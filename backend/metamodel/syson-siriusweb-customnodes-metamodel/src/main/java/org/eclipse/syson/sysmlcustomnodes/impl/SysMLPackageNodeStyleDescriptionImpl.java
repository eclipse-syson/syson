/**
 * Copyright (c) 2023, 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysmlcustomnodes.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.sirius.components.view.UserColor;
import org.eclipse.sirius.components.view.diagram.LayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Sys ML Package Node Style
 * Description</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getBorderColor <em>Border
 * Color</em>}</li>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getBorderRadius <em>Border
 * Radius</em>}</li>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getBorderSize <em>Border
 * Size</em>}</li>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getBorderLineStyle <em>Border
 * Line Style</em>}</li>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getChildrenLayoutStrategy
 * <em>Children Layout Strategy</em>}</li>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getDescription
 * <em>Description</em>}</li>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl#getBackground
 * <em>Background</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SysMLPackageNodeStyleDescriptionImpl extends MinimalEObjectImpl.Container implements SysMLPackageNodeStyleDescription {
    /**
     * The cached value of the '{@link #getBorderColor() <em>Border Color</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getBorderColor()
     * @generated
     * @ordered
     */
    protected UserColor borderColor;

    /**
     * The default value of the '{@link #getBorderRadius() <em>Border Radius</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getBorderRadius()
     * @generated
     * @ordered
     */
    protected static final int BORDER_RADIUS_EDEFAULT = 3;

    /**
     * The cached value of the '{@link #getBorderRadius() <em>Border Radius</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getBorderRadius()
     * @generated
     * @ordered
     */
    protected int borderRadius = BORDER_RADIUS_EDEFAULT;

    /**
     * The default value of the '{@link #getBorderSize() <em>Border Size</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getBorderSize()
     * @generated
     * @ordered
     */
    protected static final int BORDER_SIZE_EDEFAULT = 1;

    /**
     * The cached value of the '{@link #getBorderSize() <em>Border Size</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getBorderSize()
     * @generated
     * @ordered
     */
    protected int borderSize = BORDER_SIZE_EDEFAULT;

    /**
     * The default value of the '{@link #getBorderLineStyle() <em>Border Line Style</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getBorderLineStyle()
     * @generated
     * @ordered
     */
    protected static final LineStyle BORDER_LINE_STYLE_EDEFAULT = LineStyle.SOLID;

    /**
     * The cached value of the '{@link #getBorderLineStyle() <em>Border Line Style</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getBorderLineStyle()
     * @generated
     * @ordered
     */
    protected LineStyle borderLineStyle = BORDER_LINE_STYLE_EDEFAULT;

    /**
     * The cached value of the '{@link #getChildrenLayoutStrategy() <em>Children Layout Strategy</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getChildrenLayoutStrategy()
     * @generated
     * @ordered
     */
    protected LayoutStrategyDescription childrenLayoutStrategy;

    /**
     * The default value of the '{@link #getDescription() <em>Description</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getDescription()
     * @generated
     * @ordered
     */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getDescription()
     * @generated
     * @ordered
     */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
     * The cached value of the '{@link #getBackground() <em>Background</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getBackground()
     * @generated
     * @ordered
     */
    protected UserColor background;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected SysMLPackageNodeStyleDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysMLCustomnodesPackage.Literals.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UserColor getBorderColor() {
        if (this.borderColor != null && this.borderColor.eIsProxy()) {
            InternalEObject oldBorderColor = (InternalEObject) this.borderColor;
            this.borderColor = (UserColor) this.eResolveProxy(oldBorderColor);
            if (this.borderColor != oldBorderColor) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR, oldBorderColor, this.borderColor));
            }
        }
        return this.borderColor;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public UserColor basicGetBorderColor() {
        return this.borderColor;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBorderColor(UserColor newBorderColor) {
        UserColor oldBorderColor = this.borderColor;
        this.borderColor = newBorderColor;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR, oldBorderColor, this.borderColor));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int getBorderRadius() {
        return this.borderRadius;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBorderRadius(int newBorderRadius) {
        int oldBorderRadius = this.borderRadius;
        this.borderRadius = newBorderRadius;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS, oldBorderRadius, this.borderRadius));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int getBorderSize() {
        return this.borderSize;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBorderSize(int newBorderSize) {
        int oldBorderSize = this.borderSize;
        this.borderSize = newBorderSize;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE, oldBorderSize, this.borderSize));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LineStyle getBorderLineStyle() {
        return this.borderLineStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBorderLineStyle(LineStyle newBorderLineStyle) {
        LineStyle oldBorderLineStyle = this.borderLineStyle;
        this.borderLineStyle = newBorderLineStyle == null ? BORDER_LINE_STYLE_EDEFAULT : newBorderLineStyle;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE, oldBorderLineStyle, this.borderLineStyle));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LayoutStrategyDescription getChildrenLayoutStrategy() {
        return this.childrenLayoutStrategy;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetChildrenLayoutStrategy(LayoutStrategyDescription newChildrenLayoutStrategy, NotificationChain msgs) {
        LayoutStrategyDescription oldChildrenLayoutStrategy = this.childrenLayoutStrategy;
        this.childrenLayoutStrategy = newChildrenLayoutStrategy;
        if (this.eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY,
                    oldChildrenLayoutStrategy, newChildrenLayoutStrategy);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setChildrenLayoutStrategy(LayoutStrategyDescription newChildrenLayoutStrategy) {
        if (newChildrenLayoutStrategy != this.childrenLayoutStrategy) {
            NotificationChain msgs = null;
            if (this.childrenLayoutStrategy != null)
                msgs = ((InternalEObject) this.childrenLayoutStrategy).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY,
                        null, msgs);
            if (newChildrenLayoutStrategy != null)
                msgs = ((InternalEObject) newChildrenLayoutStrategy).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY,
                        null, msgs);
            msgs = this.basicSetChildrenLayoutStrategy(newChildrenLayoutStrategy, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY, newChildrenLayoutStrategy,
                    newChildrenLayoutStrategy));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDescription(String newDescription) {
        String oldDescription = this.description;
        this.description = newDescription;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__DESCRIPTION, oldDescription, this.description));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UserColor getBackground() {
        if (this.background != null && this.background.eIsProxy()) {
            InternalEObject oldBackground = (InternalEObject) this.background;
            this.background = (UserColor) this.eResolveProxy(oldBackground);
            if (this.background != oldBackground) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND, oldBackground, this.background));
            }
        }
        return this.background;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public UserColor basicGetBackground() {
        return this.background;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBackground(UserColor newBackground) {
        UserColor oldBackground = this.background;
        this.background = newBackground;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND, oldBackground, this.background));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY:
                return this.basicSetChildrenLayoutStrategy(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR:
                if (resolve)
                    return this.getBorderColor();
                return this.basicGetBorderColor();
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS:
                return this.getBorderRadius();
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE:
                return this.getBorderSize();
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE:
                return this.getBorderLineStyle();
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY:
                return this.getChildrenLayoutStrategy();
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__DESCRIPTION:
                return this.getDescription();
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND:
                if (resolve)
                    return this.getBackground();
                return this.basicGetBackground();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR:
                this.setBorderColor((UserColor) newValue);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS:
                this.setBorderRadius((Integer) newValue);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE:
                this.setBorderSize((Integer) newValue);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE:
                this.setBorderLineStyle((LineStyle) newValue);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY:
                this.setChildrenLayoutStrategy((LayoutStrategyDescription) newValue);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__DESCRIPTION:
                this.setDescription((String) newValue);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND:
                this.setBackground((UserColor) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR:
                this.setBorderColor((UserColor) null);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS:
                this.setBorderRadius(BORDER_RADIUS_EDEFAULT);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE:
                this.setBorderSize(BORDER_SIZE_EDEFAULT);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE:
                this.setBorderLineStyle(BORDER_LINE_STYLE_EDEFAULT);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY:
                this.setChildrenLayoutStrategy((LayoutStrategyDescription) null);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__DESCRIPTION:
                this.setDescription(DESCRIPTION_EDEFAULT);
                return;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND:
                this.setBackground((UserColor) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR:
                return this.borderColor != null;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS:
                return this.borderRadius != BORDER_RADIUS_EDEFAULT;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE:
                return this.borderSize != BORDER_SIZE_EDEFAULT;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE:
                return this.borderLineStyle != BORDER_LINE_STYLE_EDEFAULT;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY:
                return this.childrenLayoutStrategy != null;
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? this.description != null : !DESCRIPTION_EDEFAULT.equals(this.description);
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND:
                return this.background != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (borderRadius: ");
        result.append(this.borderRadius);
        result.append(", borderSize: ");
        result.append(this.borderSize);
        result.append(", borderLineStyle: ");
        result.append(this.borderLineStyle);
        result.append(", description: ");
        result.append(this.description);
        result.append(')');
        return result.toString();
    }

} // SysMLPackageNodeStyleDescriptionImpl
