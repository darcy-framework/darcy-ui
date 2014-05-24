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
import java.lang.reflect.Method;

import com.redhat.darcy.ui.elements.Element;

public class ElementInvocationHandler implements InvocationHandler {
    private Class<? extends Element> type;
    private Locator locator;
    private View view;
    
    private Element cachedElement;
    
    public ElementInvocationHandler(Class<? extends Element> type, Locator locator) {
        this.type = type;
        this.locator = locator;
    }
    
    public ElementInvocationHandler(Class<? extends Element> type, Locator locator, View view) {
        this.type = type;
        this.locator = locator;
        this.view = view;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setView".equals(method.getName())) {
            view = (View) args[0];
            
            return null;
        }
        
        if (view == null) {
            throw new NullViewException();
        }
        
        ViewContext context = view.getContext();
        
        if (context == null) {
            throw new NullContextException();
        }
        
        if (cachedElement == null) {
            try {
                cachedElement = context.findElement(type, locator);
            } catch (Exception e) {
                // We couldn't find the element. If all we want to know is if the element is
                // displayed or not, well we can answer that question: no.
                if ("isDisplayed".equals(method.getName())) {
                    e.printStackTrace();
                    return false;
                } else {
                    // Otherwise, we were trying to act on an element we can't find.
                    throw new NotFoundException(type, locator, e);
                }
            }
        }
        
        return method.invoke(cachedElement, args);
    }
}
