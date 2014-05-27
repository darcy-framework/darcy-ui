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

public class ForwardingElementContextInvocationHandler implements InvocationHandler {
    private final ForwardingElementContext context;
    
    public ForwardingElementContextInvocationHandler(
            ForwardingElementContext forwardingElementContext) {
        this.context = forwardingElementContext;
    }
    
    @Override
    public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(ElementContext.class)) {
            return method.invoke(context, args);
        } else {
            return method.invoke(context.getWrappedElementContext(), args);
        }
    }
    
}
