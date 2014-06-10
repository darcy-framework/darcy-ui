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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.redhat.darcy.DarcyException;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.LazyElement;

/**
 * The InvocationHandler for proxied {@link Element}s. Provides some of the convenience-related 
 * functionality that is expected of Darcy elements.
 * <ul>
 * <li>Lazily finds elements and caches them once they are found.</li>
 * <li>Because the proxy is the thing actually tracking down the real element implementation, the 
 * proxy effectively implements {@link LazyElement} and accepts the context with which to use to 
 * find the element. (Though not always necessary -- the context may be passed in the constructor.)
 * </li>
 * <li>Prevents a call to {@link Element#isDisplayed()} from throwing an exception if there is no
 * element found with which to call isDisplayed on. Instead of throwing the exception it will simply
 * return false.</li>
 * </ul>
 * 
 * @see LazyElement
 * @see ProxyElementSelection
 */
public class ElementInvocationHandler implements InvocationHandler {
    private Class<? extends Element> type;
    private Locator locator;
    private ElementContext context;
    
    private Element cachedElement;
    
    public ElementInvocationHandler(Class<? extends Element> type, Locator locator) {
        this.type = type;
        this.locator = locator;
    }
    
    public ElementInvocationHandler(Class<? extends Element> type, Locator locator, 
            ElementContext view) {
        this.type = type;
        this.locator = locator;
        this.context = view;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setContext".equals(method.getName())) {
            context = (ElementContext) args[0];
            cachedElement = null;
            
            return null;
        }
        
        if (context == null) {
            throw new NullContextException();
        }
        
        if (cachedElement == null) {
            try {
                cachedElement = locator.find(type, context);
            } catch (Exception e) {
                // We couldn't find the element. If all we want to know is if the element is
                // displayed or not, well we can answer that question: no.
                if ("isDisplayed".equals(method.getName())) {
                    return false;
                } else {
                    // Otherwise, we were trying to act on an element we can't find.
                    throw new NotFoundException(type, locator, e);
                }
            }
        }

        try {
            return method.invoke(cachedElement, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
