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

import javax.annotation.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Like {@link ElementViewInvocationHandler}, but for Lists of Views.
 * <P>
 * One notable difference is the use of {@link NestedElementContext} instead of
 * {@link ChainedElementContext}. This is because each resulting View must be associated with one of
 * the many possible elements found by the given locator. We can't simply use the locator as the
 * parent for each View (as in {@link ChainedElementContext}), because each View could, and probably
 * would, end up wrapping the exact same element. We must first find all of the elements for the
 * given locator, and then create a View for each that is nested underneath its respective element.
 * 
 * @see ElementViewInvocationHandler
 * @see NestedElementContext
 */
public class ElementViewListInvocationHandler implements InvocationHandler {
    private final Locator parentLocator;
    private final Supplier<View> viewSupplier;
    
    private ElementContext context;
    private List<View> cachedList;
    
    /**
     * The proxy needs to also implement LazyElement (so you can call setContext(elementContext))
     * 
     * @param viewSupplier
     *            A supplier of real implementations that we will forward method calls to.
     * @param by
     */
    public ElementViewListInvocationHandler(Supplier<View> viewSupplier) {
        this(viewSupplier, null);
    }
    
    /**
     * The proxy needs to also implement LazyElement (so you can call setContext(elementContext))
     * 
     * @param viewSupplier
     *            A supplier of real implementations that we will forward method calls to.
     * @param by
     */
    public ElementViewListInvocationHandler(Supplier<View> viewSupplier, @Nullable Locator locator) {
        this(viewSupplier, locator, null);
    }
    
    public ElementViewListInvocationHandler(Supplier<View> viewSupplier, @Nullable Locator locator,
            @Nullable ElementContext context) {
        Objects.requireNonNull(viewSupplier);
        
        this.parentLocator = locator;
        this.viewSupplier = viewSupplier;
        this.context = context;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setContext".equals(method.getName())) {
            context = (ElementContext) args[0];
            cachedList = null;
            
            return null;
        }
        
        if (cachedList == null) {
            List<Element> parentElements = parentLocator.findAll(Element.class, context);
            
            cachedList = parentElements.stream()
                    .map(this::getViewForParentElement)
                    .collect(Collectors.toList());
        }

        try {
            return method.invoke(cachedList, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
    
    private View getViewForParentElement(Element parentElement) {
        return viewSupplier.get().setContext(
                NestedElementContext.makeNestedElementContext(context, parentElement));
    }
}
