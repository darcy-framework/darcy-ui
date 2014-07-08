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

import com.redhat.darcy.util.ReflectionUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * A ChainedElementContext wraps another ElementContext, but nests all element locations under some
 * other locator, effectively turning all locators passed to this context to {@link By.ByChained}
 * {@link Locator} types.
 * <P>
 * The static factory method to create a ChainedElementContext returns a proxy. In this way, the
 * resulting ChainedElementContext still implements all of the other interfaces of the wrapped 
 * ElementContext, and will only intervene when trying to find elements. ChainedElementContexts can
 * be safely casted to the other interfaces of the particular ElementContext implementation it is 
 * forwarding.
 */
public class ChainedElementContext implements ForwardingElementContext {
    private final ElementContext context;
    private final Locator parentLocator;
    
    /**
     * Takes some context, and an optional locator, and creates a new context that implements the
     * same interfaces, but nests all element locators under the parent locator.
     * <P>
     * In other words, given some View that has an element found By.id("example"), setting that
     * View's context to a NestedViewContext underneath the locator By.id("parent") will make that 
     * element found instead By.chained(By.id("parent"), By.id("example)).
     * 
     * @param context
     * @param parentLocator
     * @return
     * @see ForwardingElementContextInvocationHandler
     */
    public static ElementContext makeChainedElementContext(ElementContext context,
            @Nullable Locator parentLocator) {
        return (ElementContext) Proxy.newProxyInstance(
                ElementViewInvocationHandler.class.getClassLoader(),
                ReflectionUtil.getAllInterfaces(context).toArray(new Class[]{}),
                new ForwardingElementContextInvocationHandler(
                        new ChainedElementContext(context, parentLocator)));
    }
    
    /**
     * 
     * @param context The context to forward to.
     * @param parentLocator Can be null if this nested View does not have any "root" element.
     */
    private ChainedElementContext(ElementContext context, @Nullable Locator parentLocator) {
        Objects.requireNonNull(context);
        
        this.context = context;
        this.parentLocator = parentLocator;
    }

    @Override
    public ElementSelection find() {
        return new LocatorTransformingElementSelection(context.find(), this::getChainedLocator);
    }
    
    @Override
    public ElementContext getWrappedElementContext() {
        return context;
    }
    
    
    public Locator getParentLocator() {
        return parentLocator;
    }
    
    /**
     * Transforms a locator to a {@link By.ByChained} Locator.
     * @param locator
     * @return
     */
    private Locator getChainedLocator(Locator locator) {
        if (parentLocator == null) {
            return locator;
        } else {
            return By.chained(parentLocator, locator);
        }
    }
}
