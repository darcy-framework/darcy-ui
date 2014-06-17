/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.ui;

import com.redhat.darcy.ui.elements.Element;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Supplier;

/**
 * An {@link ElementSelection} implementation that creates proxies of the given element interface.
 * These proxies lazily find the element, and protect calls to {@link Element#isDisplayed()} from
 * throwing an exception should the element not be found.
 * 
 * @see ElementInvocationHandler
 */
public class ProxyElementSelection implements ElementSelection {
    private final ElementContext context;
    private static final ClassLoader cl = ProxyElementSelection.class.getClassLoader();
    
    public ProxyElementSelection(ElementContext context) {
        this.context = context;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> T elementOfType(Class<T> elementType, Locator locator) {
        if (!elementType.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: "
                    + elementType);
        }

        return (T) Proxy.newProxyInstance(cl, new Class[] { elementType },
                new ElementInvocationHandler(elementType, locator, context));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator) {
        if (!elementType.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: "
                    + elementType);
        }

        return (List<T>) Proxy.newProxyInstance(cl, new Class[] { List.class },
                new ElementListInvocationHandler(elementType, locator, context));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> T elementOfType(Class<T> elementType, Locator locator,
                                               T implementation) {
        if (!(implementation instanceof View)) {
            throw new IllegalArgumentException("Element implementation must also be a View. " +
                    "(" + implementation + ")");
        }

        if (!elementType.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: "
                    + elementType);
        }

        return (T) Proxy.newProxyInstance(cl, new Class[] { elementType },
                new ElementViewInvocationHandler((View) implementation, locator, context));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator,
                                                      Supplier<? extends T> implementation) {
        if (!elementType.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: "
                    + elementType);
        }

        Supplier<View> viewSupplier = () -> {
            try {
                return (View) implementation.get();
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Custom element types must also implement View. "
                        + "To override or add a fundamental element type is implementation "
                        + "specific.");
            }
        };
        
        return (List<T>) Proxy.newProxyInstance(cl, new Class[] { List.class },
                new ElementViewListInvocationHandler(viewSupplier, locator, context));
    }
    
}
