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

import static com.redhat.darcy.ui.NestedElementContext.makeNestedElementContext;

import com.redhat.darcy.ui.elements.Element;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Like {@link ViewElementHandler}, but for Lists of Views.
 * <p>
 * One notable difference is the use of {@link NestedElementContext} instead of {@link
 * ChainedElementContext}. This is because each resulting View must be associated with one of the
 * many possible elements found by the given locator. We can't simply use the locator as the parent
 * for each View (as in {@link ChainedElementContext}), because each View could, and probably would,
 * end up wrapping the exact same element. We must first find all of the elements for the given
 * locator, and then create a View for each that is nested underneath its respective element.
 *
 * @see ViewElementHandler
 * @see NestedElementContext
 */
public class ViewElementListHandler implements InvocationHandler {
    private final Locator parentLocator;
    private final Supplier<View> viewSupplier;

    private ElementContext context;
    private List<View> cachedList;

    /**
     * The proxy needs to also implement LazyElement (so you can call setContext(elementContext))
     *
     * @param viewSupplier A supplier of real implementations that we will forward method calls to.
     * @param locator
     */
    public ViewElementListHandler(Supplier<View> viewSupplier, Locator locator) {
        this.viewSupplier = Objects.requireNonNull(viewSupplier, "viewSupplier");
        this.parentLocator = Objects.requireNonNull(locator, "locator");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("setContext".equals(method.getName())) {
            context = (ElementContext) args[0];
            cachedList = null;

            return null;
        }

        if (cachedList == null) {
            cachedList = parentLocator.findAll(Element.class, context)
                    .stream()
                    .map(this::getViewForParentElement)
                    .collect(Collectors.toList());
        }

        try {
            return method.invoke(cachedList, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private View getViewForParentElement(Element parentElement) {
        View view = viewSupplier.get();
        view.setContext(makeNestedElementContext(context, parentElement));

        return view;
    }
}
