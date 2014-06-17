/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-ui.

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

import static com.redhat.darcy.ui.ChainedElementContext.makeChainedElementContext;
import static com.redhat.darcy.ui.NestedElementContext.makeNestedElementContext;

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.util.LazyList;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultElementSelection implements ElementSelection {
    private final ElementContext context;

    public DefaultElementSelection(ElementContext context) {
        this.context = context;
    }

    @Override
    public <T extends Element> T elementOfType(Class<T> elementType, Locator locator) {
        return locator.find(elementType, context);
    }

    @Override
    public <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator) {
        return locator.findAll(elementType, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> T elementOfType(Class<T> elementType, Locator locator,
            T implementation) {
        if (!(implementation instanceof View)) {
            throw new IllegalArgumentException("Element implementation must also be a View. " +
                    "(" + implementation + ")");
        }

        View view = (View) implementation;

        return (T) view.setContext(makeChainedElementContext(context, locator));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator,
            Supplier<? extends T> implementation) {
        Supplier<View> viewSupplier = () -> {
            try {
                return (View) implementation.get();
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Custom element types must also implement View. "
                        + "To override or add a fundamental element type is implementation "
                        + "specific.");
            }
        };

        return new LazyList<>(() -> locator.findAll(Element.class, context)
                .stream()
                .map(e -> viewSupplier.get().setContext(makeNestedElementContext(context, e)))
                .map(v -> (T) v)
                .collect(Collectors.toList()));
    }
}
