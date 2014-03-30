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

public class LazyElementInvocationHandler implements InvocationHandler {
    private Class<? extends Element> type;
    private By by;
    private View view;
    
    public LazyElementInvocationHandler(Class<? extends Element> type, By by) {
        this.type = type;
        this.by = by;
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
        
        return method.invoke(context.findElement(type, by), args);
    }
}
