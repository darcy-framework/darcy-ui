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

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LocatorTransformingElementSelection implements ElementSelection {
    private final ElementSelection elementSelection;
    private final UnaryOperator<Locator> locatorFunction;
    
    public LocatorTransformingElementSelection(ElementSelection elementSelection,
            UnaryOperator<Locator> locatorFunction) {
        Objects.requireNonNull(elementSelection);
        Objects.requireNonNull(locatorFunction);
        
        this.elementSelection = elementSelection;
        this.locatorFunction = locatorFunction;
    }
    
    @Override
    public <T extends Element> T elementOfType(Class<T> elementType, Locator locator) {
        return elementSelection.elementOfType(elementType, locatorFunction.apply(locator));
    }
    
    @Override
    public <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator) {
        return elementSelection.elementsOfType(elementType, locatorFunction.apply(locator));
    }
    
    @Override
    public <T extends Element & View> T elementOfType(T implementation, Locator locator) {
        return elementSelection.elementOfType(implementation, locatorFunction.apply(locator));
    }
    
    @Override
    public <T extends Element & View> List<T> elementsOfType(Supplier<T> implementation,
            Locator locator) {
        return elementSelection.elementsOfType(implementation, locatorFunction.apply(locator));
    }
    
}
