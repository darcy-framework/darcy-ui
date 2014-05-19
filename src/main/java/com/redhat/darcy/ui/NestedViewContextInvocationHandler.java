package com.redhat.darcy.ui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NestedViewContextInvocationHandler implements InvocationHandler {
    private final NestedViewContext nestedViewContext;
    
    public NestedViewContextInvocationHandler(NestedViewContext nestedViewContext) {
        this.nestedViewContext = nestedViewContext;
    }
    
    @Override
    public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(ViewContext.class)) {
            return method.invoke(nestedViewContext, args);
        } else {
            return method.invoke(nestedViewContext.getParentView().getContext(), args);
        }
    }
    
}
