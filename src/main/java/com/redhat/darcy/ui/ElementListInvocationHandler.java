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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

public class ElementListInvocationHandler implements InvocationHandler {
    private Class<? extends Element> type;
    private Locator locator;
    private ElementContext context;
    
    private List<? extends Element> cachedList;
    
    public ElementListInvocationHandler(Class<? extends Element> type, Locator locator) {
        this.type = type;
        this.locator = locator;
    }
    
    public ElementListInvocationHandler(Class<? extends Element> type, Locator locator, 
            @Nullable ElementContext view) {
        this.type = type;
        this.locator = locator;
        this.context = view;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setContext".equals(method.getName())) {
            context = (ElementContext) args[0];
            cachedList = null;
            
            return null;
        }
        
        if (context == null) {
            throw new NullContextException();
        }

        if (cachedList == null) {
            cachedList = locator.findAll(type, context);
        }
        
        return method.invoke(cachedList, args);
    }
}
