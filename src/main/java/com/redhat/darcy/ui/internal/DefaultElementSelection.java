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

package com.redhat.darcy.ui.internal;

import static com.redhat.darcy.ui.internal.ChainedElementContext.makeChainedElementContext;
import static com.redhat.darcy.ui.internal.NestedElementContext.makeNestedElementContext;

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.ElementSelection;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
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

    @Override
    public <T extends Element & View> T elementOfType(T implementation, Locator locator) {
        implementation.setContext(makeChainedElementContext(context, locator));

        return implementation;
    }

    @Override
    public <T extends Element & View> List<T> elementsOfType(Supplier<T> implementation,
            Locator locator) {
        return new LazyList<T>(() -> locator.findAll(Element.class, context)
                .stream()
                .map(e -> {
                    T view = implementation.get();
                    view.setContext(makeNestedElementContext(context, e));
                    return view;
                })
                .collect(Collectors.toList()));
    }
}