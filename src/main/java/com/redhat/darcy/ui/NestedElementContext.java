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
import com.redhat.darcy.util.ReflectionUtil;

import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * A NestedElementContext wraps another ElementContext, but nests all element locations under some
 * other element, effectively turning all locators passed to this context to {@link By.ByNested}
 * {@link Locator} types.
 * <P>
 * The static factory method to create a NestedElementContext returns a proxy. In this way, the
 * resulting NestedElementContext still implements all of the other interfaces of the wrapped 
 * ElementContext, and will only intervene when trying to find elements. NestedElementContexts can
 * be safely casted to the other interfaces of the particular ElementContext implementation it is 
 * forwarding.
 */
public class NestedElementContext implements ForwardingElementContext {
    private final ElementContext context;
    private final Element parentElement;
    
    /** 
     * Takes some context, and an optional locator, and creates a new context that implements the
     * same interfaces, but nests all element locators under the parent locator.
     * <P>
     * In other words, given some View that has an element found By.id("example"), setting that
     * View's context to a NestedViewContext underneath some parent element will make that 
     * element found instead via By.nested(parentElement, By.id("example)).
     * 
     * @param context
     * @param parentLocator
     * @return
     * @see ForwardingElementContextInvocationHandler
     */
    public static ElementContext makeNestedElementContext(ElementContext context,
            Element parentElement) {
        return (ElementContext) Proxy.newProxyInstance(
                CustomElementHandler.class.getClassLoader(),
                ReflectionUtil.getAllInterfaces(context).toArray(new Class[]{}),
                new ForwardingElementContextInvocationHandler(
                        new NestedElementContext(context, parentElement)));
    }
    
    /**
     * 
     * @param context The context to forward to.
     * @param parentElement The root element with which to find under.
     */
    private NestedElementContext(ElementContext context, Element parentElement) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(parentElement);
        
        this.context = context;
        this.parentElement = parentElement;
    }

    @Override
    public ElementSelection find() {
        return new LocatorTransformingElementSelection(context.find(), this::getNestedLocator);
    }
    
    @Override
    public ElementContext getWrappedElementContext() {
        return context;
    }
    
    public Element getParentElement() {
        return parentElement;
    }
    
    private Locator getNestedLocator(Locator locator) {
        return By.nested(parentElement, locator);
    }
}
