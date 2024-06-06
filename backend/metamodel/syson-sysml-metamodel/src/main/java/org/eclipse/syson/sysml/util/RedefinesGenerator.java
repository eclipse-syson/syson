/*******************************************************************************
 * Copyright (c) 2024 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder Generator allowing to help generating redefines from the metamodel annotations. It only helps to initiate the
 * generation, it does not handle imports, corner cases, specifications bugs...
 *
 * @author arichard
 */
public class RedefinesGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedefinesGenerator.class);

    private final String outputDirectory;

    private final String basePackage;

    public RedefinesGenerator(String outputDirectory, String basePackage) {
        this.outputDirectory = outputDirectory;
        this.basePackage = basePackage;
    }

    /**
     * Call with the following args:
     * - ${project.basedir}
     * - ${project.basedir}/src/main/java/org/eclipse/syson/sysml/impl
     * - org.eclipse.syson.sysml.impl
     * with ${project.basedir} = the absolute path to the syson-metamodel module.
     *
     * @param args
     */
    public static void main(String[] args) {
        for (String string : args) {
            LOGGER.info(string);
        }
        String javaVersion = Runtime.version().toString();
        String time = LocalDateTime.now().toString();
        LOGGER.info("********\nBuild Time: " + time + "\nJava Version: " + javaVersion + "\n********");

        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new EcoreResourceFactoryImpl());

        resourceSet.getPackageRegistry().put(GenModelPackage.eNS_URI, GenModelPackage.eINSTANCE);
        resourceSet.getResources().add(EcorePackage.eINSTANCE.eResource());
        resourceSet.getURIConverter().getURIMap().put(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), URI.createURI("http://www.eclipse.org/emf/2002/Ecore"));

        URI sysmlURI = URI.createFileURI(args[0] + "/src/main/resources/model/sysml.genmodel");
        Resource viewResource = resourceSet.getResource(sysmlURI, true);
        List<EObject> allViewContent = new ArrayList<>(viewResource.getContents());

        var gen = new RedefinesGenerator(args[1], args[2]);

        StreamSupport.stream(Spliterators.spliterator(allViewContent, Spliterator.ORDERED), false).filter(GenModel.class::isInstance).map(GenModel.class::cast).forEach(model -> {
            try {
                gen.doGen(model);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        });
        LOGGER.info("Generated!");
    }

    public void doGen(GenModel model) throws IOException {
        for (GenPackage pak : model.getGenPackages()) {
            for (GenClass clazz : pak.getGenClasses()) {
                String fileName = this.classFileName(clazz) + ".java";
                StringBuilder content = this.updatedContentWithSubsetsRedefines(clazz, this.outputDirectory, fileName);
                String contentToGenerate = content.toString();
                this.merge(this.outputDirectory, fileName, contentToGenerate);
            }
        }
    }

    private StringBuilder updatedContentWithSubsetsRedefines(GenClass clazz, String outputDirectory, String fileName) throws IOException {
        System.out.println(clazz.capName(clazz.getName()));
        StringBuilder body = new StringBuilder();
        if (Files.exists(Path.of(outputDirectory, fileName))) {
            body.append(Files.readString(Path.of(outputDirectory, fileName)));
        }
        body.replace(body.lastIndexOf("}"), body.length(), "");
        for (GenFeature feat : clazz.getGenFeatures()) {
            EStructuralFeature ecoreFeature = feat.getEcoreFeature();
            if (this.containsRedefines(ecoreFeature)) {
                EAnnotation redefinesAnnotation = this.getRedefines(ecoreFeature);
                String genGetRedefines = this.genGetRedefines(feat, redefinesAnnotation);
                if (!this.containsGeneratedMethod(body.toString(), genGetRedefines)) {
                    body.append(genGetRedefines);
                }
                if (!ecoreFeature.isMany() && !ecoreFeature.isDerived()) {
                    String genSetRedefines = this.genSetRedefines(feat, redefinesAnnotation);
                    if (!this.containsGeneratedMethod(body.toString(), genSetRedefines)) {
                        body.append(genSetRedefines);
                    }
                }
            }
        }
        body.append("""
                } // #eClassNameImpl
                """.replace("#eClassNameImpl", this.classFileName(clazz)));
        return body;
    }

    private void merge(String outDirectory, String fileName, String contentToGenerate) throws IOException {
        String beforeGen = null;
        if (Files.exists(Path.of(outDirectory, fileName))) {
            beforeGen = Files.readString(Path.of(outDirectory, fileName));
        }
        if (beforeGen != null) {
            Files.writeString(Path.of(outDirectory, fileName), contentToGenerate);
        }
    }

    private boolean containsRedefines(EStructuralFeature eSF) {
        return this.getRedefines(eSF) != null;
    }

    private EAnnotation getRedefines(EStructuralFeature eSF) {
        return eSF.getEAnnotation("redefines");
    }

    private boolean containsGeneratedMethod(String existingContent, String genMethod) {
        return existingContent.contains(genMethod);
    }

    private String classFileName(GenClass clazz) {
        return clazz.getName() + "Impl";
    }

    private String genGetRedefines(GenFeature genFeature, EAnnotation eAnnotation) {
        var genGetRedefines = new StringBuilder();
        List<EReference> redefinedFeatures = this.getRedefinedFeatures(eAnnotation);
        for (EReference eReference : redefinedFeatures) {
            genGetRedefines.append(this.genGetRedefines(genFeature, eReference));
        }
        return genGetRedefines.toString();
    }

    private String genGetRedefines(GenFeature genFeature, EReference redefinedFeature) {
        String template = """
                    /**
                     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
                     *
                     * @generated NOT
                     */
                    @Override
                    public #redefinedFeatureType #getRedefinedFeatureName() {
                    #getRedefinedFeatureBody
                    }

                """;
        return template
                .replace("#redefinedFeatureType", this.genRedefinedFeatureType(redefinedFeature))
                .replace("#getRedefinedFeatureName", this.genGetRedefinedFeatureName(redefinedFeature))
                .replace("#getRedefinedFeatureBody", this.genGetRedefinedFeatureBody(genFeature, redefinedFeature));
    }

    private String genSetRedefines(GenFeature genFeature, EAnnotation eAnnotation) {
        var genSetRedefines = new StringBuilder();
        List<EReference> redefinedFeatures = this.getRedefinedFeatures(eAnnotation);
        for (EReference eReference : redefinedFeatures) {
            if (!eReference.isMany() && !eReference.isDerived()) {
                genSetRedefines.append(this.genSetRedefines(genFeature, eReference));
            }
        }
        return genSetRedefines.toString();
    }

    private String genSetRedefines(GenFeature genFeature, EReference redefinedFeature) {
        String template = """
                   /**
                    * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
                    *
                    * @generated NOT
                    */
                   @Override
                   public void #setRedefinedFeatureName(#redefinedFeatureType #newVarRedefinedFeatureName) {
                   #setRedefinedFeatureBody
                   }

               """;
        return template
                .replace("#redefinedFeatureType", this.genRedefinedFeatureType(redefinedFeature))
                .replace("#setRedefinedFeatureName", this.genSetRedefinedFeatureName(redefinedFeature))
                .replace("#newVarRedefinedFeatureName", this.genNewVarRedefinedFeatureName(redefinedFeature))
                .replace("#setRedefinedFeatureBody", this.genSetRedefinedFeatureBody(genFeature, redefinedFeature));
    }

    private List<EReference> getRedefinedFeatures(EAnnotation eAnnotation) {
        return eAnnotation.getReferences().stream()
                .filter(EReference.class::isInstance)
                .map(EReference.class::cast)
                .toList();
    }

    private String genGetRedefiningFeatureName(String safeName) {
        var redefiningFeatureNameGet = new StringBuilder();
        redefiningFeatureNameGet.append("get");
        redefiningFeatureNameGet.append(StringUtils.capitalize(safeName));
        return redefiningFeatureNameGet.toString();
    }

    private String genSetRedefiningFeatureName(String safeName) {
        var redefiningFeatureNameSet = new StringBuilder();
        redefiningFeatureNameSet.append("set");
        redefiningFeatureNameSet.append(StringUtils.capitalize(safeName));
        return redefiningFeatureNameSet.toString();
    }

    private String genRedefinedFeatureName(EReference redefinedFeature) {
        return redefinedFeature.getName();
    }

    private String genGetRedefinedFeatureName(EReference redefinedFeature) {
        var redefinedFeatureNameGet = new StringBuilder();
        var genRedefinedFeatureName = this.genRedefinedFeatureName(redefinedFeature);
        if (genRedefinedFeatureName.isEmpty()) {
            redefinedFeatureNameGet.append("/* Feature to redefine not found - Update metamodel eAnnotation*/");
        } else {
            redefinedFeatureNameGet.append("get");
            redefinedFeatureNameGet.append(StringUtils.capitalize(genRedefinedFeatureName));
        }
        return redefinedFeatureNameGet.toString();
    }

    private String genSetRedefinedFeatureName(EReference redefinedFeature) {
        var redefinedFeatureNameSet = new StringBuilder();
        redefinedFeatureNameSet.append("set");
        redefinedFeatureNameSet.append(StringUtils.capitalize(redefinedFeature.getName()));
        if (redefinedFeatureNameSet.isEmpty()) {
            redefinedFeatureNameSet.append("/* Feature to redefine not found - Update metamodel eAnnotation*/");
        }
        return redefinedFeatureNameSet.toString();
    }

    private String genNewVarRedefinedFeatureName(EReference redefinedFeature) {
        var redefinedFeatureNameSet = new StringBuilder();
        redefinedFeatureNameSet.append("new");
        redefinedFeatureNameSet.append(StringUtils.capitalize(redefinedFeature.getName()));
        if (redefinedFeatureNameSet.isEmpty()) {
            redefinedFeatureNameSet.append("/* Feature to redefine not found - Update metamodel eAnnotation*/");
        }
        return redefinedFeatureNameSet.toString();
    }

    private String genRedefinedFeatureType(EReference redefinedFeature) {
        var redefinedFeatureNameType = new StringBuilder();
        EClassifier eType = redefinedFeature.getEType();
        if (eType != null) {
            if (redefinedFeature.isMany()) {
                redefinedFeatureNameType.append("EList<");
            }
            String typeName = eType.getName();
            if (SysmlPackage.eINSTANCE.getClass_().getName().equals(typeName)) {
                typeName = SysmlPackage.eINSTANCE.getClass_().getInstanceTypeName();
            }
            redefinedFeatureNameType.append(typeName);
            if (redefinedFeature.isMany()) {
                redefinedFeatureNameType.append(">");
            }
        }
        if (redefinedFeatureNameType.isEmpty()) {
            redefinedFeatureNameType.append("/* Feature to redefine not found - Update metamodel eAnnotation*/");
        }
        return redefinedFeatureNameType.toString();
    }

    private String genRedefiningFeatureType(GenFeature genFeature) {
        var redefiningFeatureType = new StringBuilder();
        EStructuralFeature eSF = genFeature.getEcoreFeature();
        EClassifier eType = eSF.getEType();
        if (eType != null) {
            if (eSF.isMany()) {
                redefiningFeatureType.append("EList<");
            }
            String typeName = eType.getName();
            if (SysmlPackage.eINSTANCE.getClass_().getName().equals(typeName)) {
                typeName = SysmlPackage.eINSTANCE.getClass_().getInstanceTypeName();
            }
            redefiningFeatureType.append(typeName);
            if (eSF.isMany()) {
                redefiningFeatureType.append(">");
            }
        }
        if (redefiningFeatureType.isEmpty()) {
            redefiningFeatureType.append("/* Redefining Feature type not found - Update metamodel*/");
        }
        return redefiningFeatureType.toString();
    }

    private String getPackageRedefinedFeature(EReference redefinedFeature) {
        var packageRedefinedFeature = new StringBuilder();
        EClass containingClass = redefinedFeature.getEContainingClass();
        if (containingClass != null) {
            packageRedefinedFeature.append("SysmlPackage.eINSTANCE.get");
            packageRedefinedFeature.append(containingClass.getName());
            packageRedefinedFeature.append("_");
            packageRedefinedFeature.append(StringUtils.capitalize(redefinedFeature.getName()));
            packageRedefinedFeature.append("()");
        }
        if (packageRedefinedFeature.isEmpty()) {
            packageRedefinedFeature.append("/* Feature to redefine not found - Update metamodel*/");
        }
        return packageRedefinedFeature.toString();
    }

    private String genGetRedefinedFeatureBody(GenFeature genFeature, EReference redefinedFeature) {
        StringBuilder body = new StringBuilder();
        String getRedefiningFeatureName = this.genGetRedefiningFeatureName(genFeature.getSafeName());
        if (!redefinedFeature.isMany()) {
            body.append("""
                            return this.#getRedefiningFeatureName();\
                        """
                    .replace("#getRedefiningFeatureName", getRedefiningFeatureName));
        } else {
            String redefinedFeatureType = this.genRedefinedFeatureType(redefinedFeature);
            String redefinedFeatureName = this.genRedefinedFeatureName(redefinedFeature);
            String redefiningFeatureName = genFeature.getSafeName();
            String redefiningFeatureType = this.genRedefiningFeatureType(genFeature);
            String addOrAddAll = "add";
            if (redefiningFeatureType.startsWith("EList<")) {
                addOrAddAll = "addAll";
            }
            String getRedefinedFeatureName = this.genGetRedefinedFeatureName(redefinedFeature);
            String packageRedefinedFeature = this.getPackageRedefinedFeature(redefinedFeature);
            body.append("""
                            #redefinedFeatureType #listName = new BasicEList<>();
                                #redefiningFeatureType #redefiningFeatureName = this.#getRedefiningFeatureName();
                                if (#redefiningFeatureName != null) {
                                    #listName.#addOrAddAll(#redefiningFeatureName);
                                }
                                return new EcoreEList.UnmodifiableEList<>(this, #packageRedefinedFeature, #listName.size(), #listName.toArray());\
                        """
                    .replace("#redefinedFeatureType", redefinedFeatureType)
                    .replace("#listName", redefinedFeatureName + "s")
                    .replace("#redefiningFeatureType", redefiningFeatureType)
                    .replace("#redefiningFeatureName", redefiningFeatureName)
                    .replace("#addOrAddAll", addOrAddAll)
                    .replace("#getRedefiningFeatureName", getRedefiningFeatureName)
                    .replace("#packageRedefinedFeature", packageRedefinedFeature));
        }
        return body.toString();
    }

    private String genSetRedefinedFeatureBody(GenFeature genFeature, EReference redefinedFeature) {
        StringBuilder body = new StringBuilder();
        String setRedefiningFeatureName = this.genSetRedefiningFeatureName(genFeature.getSafeName());
        String newVarRedefinedFeatureName = this.genNewVarRedefinedFeatureName(redefinedFeature);
        String redefinedFeatureType = this.genRedefinedFeatureType(redefinedFeature);
        String redefiningFeatureType = this.genRedefiningFeatureType(genFeature);

        if (!redefinedFeatureType.equals(redefiningFeatureType)) {
            body.append("""
                            if (#newVarRedefinedFeatureName instanceof #redefiningFeatureType #newVarRedefinedFeatureName#redefiningFeatureType) {
                                    this.#setRedefiningFeatureName(#newVarRedefinedFeatureName#redefiningFeatureType);
                                }\
                        """
                    .replace("#newVarRedefinedFeatureName", newVarRedefinedFeatureName)
                    .replace("#redefiningFeatureType", redefiningFeatureType)
                    .replace("#setRedefiningFeatureName", setRedefiningFeatureName));
        } else {
            body.append("""
                            this.#setRedefiningFeatureName(#newVarRedefinedFeatureName);\
                        """
                    .replace("#setRedefiningFeatureName", setRedefiningFeatureName)
                    .replace("#newVarRedefinedFeatureName", newVarRedefinedFeatureName));
        }
        return body.toString();
    }
}
