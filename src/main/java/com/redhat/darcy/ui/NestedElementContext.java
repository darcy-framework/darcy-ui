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
import java.util.List;
import java.util.Objects;

public class NestedElementContext implements ForwardingElementContext {
    private final ElementContext context;
    private final Element parentElement;
    
    /** 
     * Takes some context, and an optional locator, and creates a new context that implements the
     * same interfaces, but nests all element locators under the parent locator.
     * <P>
     * In other words, given some View that has an element found By.id("example"), setting that
     * View's context to a NestedViewContext underneath the locator By.id("parent") will make that 
     * element found instead By.chained(By.id("parent"), By.id("example)).
     * @param context
     * @param parentLocator
     * @return
     */
    public static ElementContext makeNestedElementContext(ElementContext context,
            Element parentElement) {
        return (ViewContext) Proxy.newProxyInstance(
                ElementViewInvocationHandler.class.getClassLoader(), 
                ReflectionUtil.getAllInterfaces(context).toArray(new Class[]{}), 
                new ForwardingElementContextInvocationHandler(
                        new NestedElementContext(context, parentElement)));
    }
    
    /**
     * 
     * @param context The context to forward to.
     * @param parentBy Can be null if this nested View does not have any "root" element.
     */
    private NestedElementContext(ElementContext context, Element parentElement) {
        Objects.requireNonNull(context);
        
        this.context = context;
        this.parentElement = parentElement;
    }

    @Override
    public ElementSelection element() {
        return new LocatorTransformingElementSelection(context.element(), this::getNestedLocator);
    }

    @Override
    public <T extends Element> List<T> findElements(Class<T> type, Locator locator) {
        return context.findElements(type, getNestedLocator(locator));
    }

    @Override
    public <T extends Element> T findElement(Class<T> type, Locator locator) {
        return context.findElement(type, getNestedLocator(locator));
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
