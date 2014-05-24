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

import javax.annotation.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * For Views within Views that are created statically and assigned to their "owning" View via
 * reflection and a proxy. The proxy can be cast to LazyElement, and the invocation handler will
 * intercept the call to setView (defined by LazyElement interface), and without the actual child
 * View implementation knowing about it, set its context to be a ViewContext proxy itself: one that
 * defers finding elements to a NestedViewContext, but otherwise forwards calls to the parent
 * context.
 */
public class ElementViewInvocationHandler implements InvocationHandler {
    private final Locator parentLocator;
    private final View view;
    
    /**
     * The proxy needs to also implement LazyElement (so you can call setContext(elementContext))
     * 
     * @param view
     *            A real implementation that we will forward method calls to.
     * @param by
     */
    public ElementViewInvocationHandler(View view) {
        this(view, null);
    }
    
    /**
     * The proxy needs to also implement LazyElement (so you can call setContext(elementContext))
     * 
     * @param view
     *            A real implementation that we will forward method calls to.
     * @param by
     */
    public ElementViewInvocationHandler(View view, @Nullable Locator locator) {
        this(view, locator, null);
    }
    
    public ElementViewInvocationHandler(View view, @Nullable Locator locator, 
            @Nullable ElementContext context) {
        Objects.requireNonNull(view);
        
        this.parentLocator = locator;
        this.view = view;
        
        if (context != null) {
            setContext(context);
        }
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setContext".equals(method.getName())) {
            setContext((ViewContext) args[0]);
            
            return null;
        }
        
        return method.invoke(view, args);
    }
    
    private void setContext(ElementContext context) {
        view.setContext(ChainedElementContext.makeChainedElementContext(context,
                parentLocator));
    }
}
