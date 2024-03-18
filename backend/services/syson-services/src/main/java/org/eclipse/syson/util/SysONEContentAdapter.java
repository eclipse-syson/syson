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
package org.eclipse.syson.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;

/**
 * EContentAdapter for SysON. Allow to cache SysML elements by their type.
 * 
 * @author arichard
 */
public class SysONEContentAdapter extends EContentAdapter {

    private final Map<EClass, List<EObject>> cache = new HashMap<>();

    public Map<EClass, List<EObject>> getCache() {
        return this.cache;
    }

    @Override
    protected void addAdapter(Notifier notifier) {
        super.addAdapter(notifier);
        if (notifier instanceof Element element && !(notifier instanceof Membership)) {
            EClass eClass = element.eClass();
            List<EObject> value;
            if (this.cache.containsKey(eClass)) {
                value = this.cache.get(eClass);
            } else {
                value = new ArrayList<EObject>();
            }
            value.add(element);
            this.cache.put(eClass, value);
        }
    }

    @Override
    protected void removeAdapter(Notifier notifier) {
        if (notifier instanceof Element element && !(notifier instanceof Membership)) {
            EClass eClass = element.eClass();
            List<EObject> value;
            if (this.cache.containsKey(eClass)) {
                value = this.cache.get(eClass);
                value.remove(element);
                this.cache.put(eClass, value);
            }
        }
        super.removeAdapter(notifier);
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return SysONEContentAdapter.class.equals(type);
    }
}
