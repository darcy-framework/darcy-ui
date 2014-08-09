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

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.ViewElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.synq.Condition;

import java.util.function.UnaryOperator;

public abstract class AbstractViewElement extends AbstractView implements ViewElement {
    /**
     * The parent element of this ViewElement, should one be present.
     *
     * <p>If your ViewElement is nested under some locator, but there is no specific element at that
     * locator, then this parent element will never be present.
     */
    protected final Element parent;

    private final UnaryOperator<Locator> byNested;

    /**
     * Creates a nested View underneath some parent Locator.
     *
     * @see #nested(com.redhat.darcy.ui.api.Locator)
     */
    protected AbstractViewElement(Locator parent) {
        // How to specify element type? Is this ever going to be needed?
        this(Elements.element(parent), l -> By.chained(parent, l));
    }

    /**
     * Creates a nested View underneath some parent element.
     *
     * @see #nested(com.redhat.darcy.ui.api.Locator)
     */
    protected AbstractViewElement(Element parent) {
        this(parent, l -> By.nested(parent, l));
    }

    private AbstractViewElement(Element parent, UnaryOperator<Locator> byNested) {
        super();

        this.parent = parent;
        this.byNested = byNested;
    }

    /**
     * Determines whether or not this ViewElement is displayed by querying
     * {@link com.redhat.darcy.ui.api.elements.Element#isDisplayed()} on all of the required
     * element fields (as annotated by {@link com.redhat.darcy.ui.annotations.Require},
     * {@link com.redhat.darcy.ui.annotations.RequireAll}, and
     * {@link com.redhat.darcy.ui.annotations.NotRequired}.
     *
     * <p>By default, the {@link #parent} element is not required. If you wish to require it, you
     * can either override this method or add your own parent element field that is annotated as
     * required.
     */
    @Override
    public boolean isDisplayed() {
        return analyzer.getDisplayConditions().stream().allMatch(Condition::isMet);
    }

    /**
     * Determines whether or not this ViewElement is present by querying
     * {@link com.redhat.darcy.ui.api.elements.Findable#isPresent()} on all of the required
     * findable fields (as annotated by {@link com.redhat.darcy.ui.annotations.Require},
     * {@link com.redhat.darcy.ui.annotations.RequireAll}, and
     * {@link com.redhat.darcy.ui.annotations.NotRequired}.
     *
     * <p>By default, the {@link #parent} element is not required. If you wish to require it, you
     * can either override this method or add your own parent element field that is annotated as
     * required.
     */
    @Override
    public boolean isPresent() {
        return analyzer.getIsPresentConditions().stream().allMatch(Condition::isMet);
    }

    /**
     * Returns a locator that is nested under the parent element. This will choose whether to nest
     * under a locator or nest under an element reference based on the constructor used.
     *
     * @see #AbstractViewElement(com.redhat.darcy.ui.api.elements.Element)
     * @see #AbstractViewElement(com.redhat.darcy.ui.api.Locator)
     */
    protected final Locator nested(Locator locator) {
        return byNested.apply(locator);
    }
}
