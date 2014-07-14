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

package com.redhat.darcy.ui.elements;

import com.redhat.darcy.ui.ViewElementHandler;
import com.redhat.darcy.ui.ViewElementListHandler;
import com.redhat.darcy.ui.ElementHandler;
import com.redhat.darcy.ui.ElementListHandler;
import com.redhat.darcy.ui.LazyElement;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Supplier;

/**
 * Static factories for the fundamental UI elements. Specifically, these return proxy instances of
 * those elements, so that they may be defined statically and loaded lazily.
 * 
 * @see {@link com.redhat.darcy.ui.LazyElement}
 * @see {@link com.redhat.darcy.ui.ElementHandler}
 *
 */
public abstract class Elements {
    /**
     * Looks up a automation-library-specific implementation for that element type, assuming an
     * implementation is registered for that class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Element> T element(Class<T> type, Locator locator) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: " + type);
        }

        InvocationHandler invocationHandler = new ElementHandler(type, locator);
        
        return (T) Proxy.newProxyInstance(Elements.class.getClassLoader(), 
                new Class[] { type, LazyElement.class },
                invocationHandler);
    }
    /**
     * Looks up a automation-library-specific implementation for that element type, assuming an
     * implementation is registered for that class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Element> List<T> elements(Class<T> type, Locator locator) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: " + type);
        }

        InvocationHandler invocationHandler = new ElementListHandler(type, locator);
        
        return (List<T>) Proxy.newProxyInstance(Elements.class.getClassLoader(), 
                new Class[] { List.class, LazyElement.class },
                invocationHandler);
    }
    
    /**
     * Wraps an element implementation in a proxy so that it may be assigned to a parent View 
     * lazily, like elements.
     * <P>
     * The provided implementation must, itself, be a View. If you want to override fundamental UI
     * elements, this is done at the automation-library wrapper level and specific to that 
     * ViewContext implementation.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Element> T element(Class<T> type, Locator locator,
            T implementation) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: " + type);
        }

        if (!View.class.isAssignableFrom(implementation.getClass())) {
            throw new IllegalArgumentException("Implementation must be a View. If you wish to " +
                    "override fundamental Element types, this must be done at the automation " +
                    "library level.");
        }

        InvocationHandler invocationHandler = new ViewElementHandler((View) implementation, locator);

        return (T) Proxy.newProxyInstance(Elements.class.getClassLoader(),
                new Class[] { type, LazyElement.class },
                invocationHandler);
    }

    /**
     * Wraps an element implementation in a proxy so that it may be assigned to a parent View
     * lazily, like elements.
     * <P>
     * The provided implementation must, itself, be a View. If you want to override fundamental UI
     * elements, this is done at the automation-library wrapper level and specific to that
     * ViewContext implementation.
     */
    // The Class<T> type param is not really necessary but it makes the API consistent with creating
    // a single ViewElement field, and allows the returned list type to be of a parent interface.
    @SuppressWarnings("unchecked")
    public static <T extends Element> List<T> elements(@SuppressWarnings("unused") Class<T> type,
            Locator locator, Supplier<? extends T> implementation) {
        Supplier<View> viewSupplier = () -> {
            try {
                return (View) implementation.get();
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Implementation must be a View. If you wish to " +
                        "override fundamental Element types, this must be done at the automation " +
                        "library level.");
            }
        };

        InvocationHandler invocationHandler = new ViewElementListHandler(viewSupplier, locator);

        return (List<T>) Proxy.newProxyInstance(Elements.class.getClassLoader(),
                new Class[] { List.class, LazyElement.class },
                invocationHandler);
    }
    
    public static Element element(Locator locator) {
        return element(Element.class, locator);
    }
    
    public static List<Element> elements(Locator locator) {
        return elements(Element.class, locator);
    }
    
    public static TextInput textInput(Locator locator) {
        return element(TextInput.class, locator);
    }
    
    public static List<TextInput> textInputs(Locator locator) {
        return elements(TextInput.class, locator);
    }
    
    public static Button button(Locator locator) {
        return element(Button.class, locator);
    }
    
    public static List<Button> buttons(Locator locator) {
        return elements(Button.class, locator);
    }
    
    public static Link link(Locator locator) {
        return element(Link.class, locator);
    }
    
    public static List<Link> links(Locator locator) {
        return elements(Link.class, locator);
    }
    
    public static Label label(Locator locator) {
        return element(Label.class, locator);
    }
    
    public static List<Label> labels(Locator locator) {
        return elements(Label.class, locator);
    }
    
    public static Select select(Locator locator) {
        return element(Select.class, locator);
    }
    
    public static List<Select> selects(Locator locator) {
        return elements(Select.class, locator);
    }
}
