/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-ui.

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

package com.redhat.darcy.ui.internal;

import com.redhat.darcy.ui.NullContextException;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * The InvocationHandler for proxied {@link Element}s. Provides some of the convenience-related 
 * functionality that is expected of Darcy elements.
 * <ul>
 * <li>Lazily finds elements and caches them once they are found.</li>
 * <li>Because the proxy is the thing actually tracking down the real element implementation, the 
 * proxy effectively implements {@link com.redhat.darcy.ui.api.HasElementContext} and accepts the context with which to use to
 * find the element. (Though not always necessary -- the context may be passed in the constructor.)
 * </li>
 * </ul>
 * 
 * @see com.redhat.darcy.ui.api.HasElementContext
 * @see com.redhat.darcy.ui.Elements
 * @see com.redhat.darcy.ui.AbstractView
 */
public class ElementHandler implements InvocationHandler {
    private Class<? extends Element> type;
    private Locator locator;
    private ElementContext context;
    
    private Element cachedElement;
    
    public ElementHandler(Class<? extends Element> type, Locator locator) {
        this.type = Objects.requireNonNull(type);
        this.locator = Objects.requireNonNull(locator);
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
            cachedElement = locator.find(type, context);
        }

        if ("getWrappedElement".equals(method.getName())) {
            return cachedElement;
        }

        try {
            return method.invoke(cachedElement, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
