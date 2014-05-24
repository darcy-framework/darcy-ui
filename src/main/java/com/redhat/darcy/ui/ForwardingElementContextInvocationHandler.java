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
