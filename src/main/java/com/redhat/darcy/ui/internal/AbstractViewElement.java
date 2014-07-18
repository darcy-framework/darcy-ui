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

import com.redhat.darcy.ui.AbstractView;
import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;

import java.util.function.UnaryOperator;

public class AbstractViewElement extends AbstractView implements Element {
    protected Element parent;

    private final UnaryOperator<Locator> locatorTransform;

    public AbstractViewElement(Locator parent) {
        locatorTransform = l -> By.chained(parent, l);
        this.parent = getContext().find().element(parent);
    }

    public AbstractViewElement(Element parent) {
        locatorTransform = l -> By.nested(parent, l);
        this.parent = parent;
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    protected Locator nested(Locator locator) {
        return locatorTransform.apply(locator);
    }
}
