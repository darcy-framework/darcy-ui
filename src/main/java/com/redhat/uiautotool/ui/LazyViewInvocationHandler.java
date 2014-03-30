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

package com.redhat.uiautotool.ui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * For Views within Views that are created statically and assigned to their "owning" View via
 * reflection and a proxy. The proxy can be cast to LazyElement, and the invocation handler will 
 * intercept the call to setView (defined by LazyElement interface), and without the actual child
 * View implementation knowning about it, set its context to be a NestedViewContext, which will 
 * handle finding elements for the child View.
 * 
 * @author ahenning
 *
 */
public class LazyViewInvocationHandler implements InvocationHandler {
    private By by;
    private View view;
    
    /**
     * The proxy needs to also implement LazyElement (so you can call setView(parentView))
     * @param view A real implementation that we will forward method calls to.
     * @param by
     */
    public LazyViewInvocationHandler(View view) {
        this(view, null);
    }
    
    /**
     * The proxy needs to also implement LazyElement (so you can call setView(parentView))
     * @param view A real implementation that we will forward method calls to.
     * @param by
     */
    public LazyViewInvocationHandler(View view, By by) {
        this.by = by;
        this.view = view;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setView".equals(method.getName())) {
            // Set the "owning" or "parent" View via a NestedViewContext
            view.setContext(new NestedViewContext((View) args[0], by));
            
            return null;
        }
        
        return method.invoke(view, args);
    }
}
