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
package org.eclipse.syson.sysml.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Extends {@link ObjectFinder} with caching capabilities to enhance lookup performance.
 *
 * @author gescande
 */
public class CachedObjectFinder extends ObjectFinder {

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = this.readWriteLock.readLock();

    private Lock writeLock = this.readWriteLock.writeLock();

    private final SortedMap<Integer, EObject> cache = Collections.synchronizedSortedMap(new TreeMap<>());

    @Override
    public void addImportMember(final String importString) {
        this.writeLock.lock();
        try {
            this.cache.clear();
        } finally {
            this.writeLock.unlock();
        }
        super.addImportMember(importString);
    }

    @Override
    public void addImportNamespace(final String importString) {
        this.writeLock.lock();
        try {
            this.cache.clear();
        } finally {
            this.writeLock.unlock();
        }
        super.addImportNamespace(importString);
    }

    @Override
    public void putEObjectKey(final EObject eObject, String key) {
        this.writeLock.lock();
        try {
            this.cache.clear();
        } finally {
            this.writeLock.unlock();
        }
        super.putEObjectKey(eObject, key);
    }

    @Override
    public EObject findObject(final MappingElement mapping, JsonNode jsonNode, final EClass type) {

        EObject result = null;
        int hash = jsonNode.toString().hashCode();
        if (type != null) {
            hash += type.getName().hashCode();
        }

        this.readLock.lock();
        try {
            result = this.cache.get(hash);
        } finally {
            this.readLock.unlock();
        }

        if (result == null) {
            result = super.findObject(mapping, jsonNode, type);
            this.writeLock.lock();
            try {
                this.cache.put(hash, result);
            } finally {
                this.writeLock.unlock();
            }
        }

        return result;
    }
}
