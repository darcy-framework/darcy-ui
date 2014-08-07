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

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.synq.Condition;

import java.util.List;
import java.util.function.UnaryOperator;

public abstract class AbstractViewElement extends AbstractView implements Element {
    protected final Element parent;

    private final UnaryOperator<Locator> locatorTransform;

    private ElementContext context;

    protected AbstractViewElement(Locator parent) {
        // How to specify element type? Is this ever going to be needed?
        this(Elements.element(parent), l -> By.chained(parent, l));
    }

    protected AbstractViewElement(Element parent) {
        this(parent, l -> By.nested(parent, l));
    }

    private AbstractViewElement(Element parent, UnaryOperator<Locator> locatorTransform) {
        super();

        this.parent = parent;
        this.locatorTransform = locatorTransform;
    }

    @Override
    public boolean isDisplayed() {
        return analyzer.getDisplayConditions().stream().allMatch(Condition::isMet);
    }

    @Override
    public boolean isPresent() {
        return analyzer.getIsPresentConditions().stream().allMatch(Condition::isMet);
    }

    protected final Locator nested(Locator locator) {
        return locatorTransform.apply(locator);
    }
}
