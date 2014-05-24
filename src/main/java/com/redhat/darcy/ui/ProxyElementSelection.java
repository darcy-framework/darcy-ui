package com.redhat.darcy.ui;

import com.redhat.darcy.ui.elements.Element;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Supplier;

public class ProxyElementSelection implements ElementSelection {
    private final ElementContext context;
    private static final ClassLoader cl = ProxyElementSelection.class.getClassLoader();
    
    public ProxyElementSelection(ElementContext context) {
        this.context = context;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> T ofType(Class<T> elementType, Locator locator) {
        return (T) Proxy.newProxyInstance(cl, new Class[] { elementType },
                new ElementInvocationHandler(elementType, locator, context));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> List<T> listOfType(Class<T> elementType, Locator locator) {
        return (List<T>) Proxy.newProxyInstance(cl, new Class[] { elementType },
                new ElementListInvocationHandler(elementType, locator, context));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> T ofType(Class<T> elementType, Locator locator, T implementation) {
        return (T) Proxy.newProxyInstance(cl, new Class[] { elementType },
                new ElementViewInvocationHandler((View) implementation, locator, context));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> List<T> listOfType(Class<T> elementType, Locator locator,
            Supplier<? extends T> implementation) {
        Supplier<View> viewSupplier;
        
        try {
            // TODO: Not sure if this cast will work
            viewSupplier = (Supplier<View>) implementation;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Custom element types must also implement View. "
                    + "To override or add a fundamental element type is implementation specific.");
        }
        
        return (List<T>) Proxy.newProxyInstance(cl, new Class[] { elementType },
                new ElementViewListInvocationHandler(viewSupplier, locator, context));
    }
    
}
