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

import com.redhat.darcy.util.ReflectionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * For Views within Views that are created statically and assigned to their "owning" View via
 * reflection and a proxy. The proxy can be cast to LazyElement, and the invocation handler will 
 * intercept the call to setView (defined by LazyElement interface), and without the actual child
 * View implementation knowing about it, set its context to be a ViewContext proxy itself: 
 * one that defers finding elements to a NestedViewContext, but otherwise forwards calls to the 
 * parent context.
 */
public class LazyViewInvocationHandler implements InvocationHandler {
    private Locator locator;
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
    public LazyViewInvocationHandler(View view, Locator locator) {
        this.locator = locator;
        this.view = view;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setView".equals(method.getName())) {
            View parentView = (View) args[0];
            
            // Create a proxy that implements everything that the parent view's context implements,
            // but intercepts calls to find elements to the NestedViewContext.
            ViewContext nestedViewContext = (ViewContext) Proxy.newProxyInstance(
                    LazyViewInvocationHandler.class.getClassLoader(), 
                    ReflectionUtil.getAllInterfaces(parentView.getContext()).toArray(new Class[]{}), 
                    new NestedViewContextInvocationHandler(
                            new NestedViewContext(parentView, locator)));
            
            view.setContext(nestedViewContext);
            
            return null;
        }
        
        return method.invoke(view, args);
    }
}
