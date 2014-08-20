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

/**
 * Like {@link com.redhat.darcy.ui.AbstractView AbstractView}, but instead of just partially
 * implementing a {@link com.redhat.darcy.ui.api.View View}, implements
 * {@link com.redhat.darcy.ui.api.ViewElement ViewElement}, which implements both
 * {@link com.redhat.darcy.ui.api.View View} and
 * {@link com.redhat.darcy.ui.api.elements.Element Element}. It does this using the familiar
 * {@link com.redhat.darcy.ui.annotations.Require @Require},
 * {@link com.redhat.darcy.ui.annotations.RequireAll @RequireAll}, and
 * {@link com.redhat.darcy.ui.annotations.NotRequired @NotRequired} annotations.
 * {@link #isDisplayed()} checks that all required elements are displayed.
 * {@link #isPresent()} checks that all required findables are present.
 * {@link #isLoaded()} works just like {@link com.redhat.darcy.ui.AbstractView}.
 *
 * <p>Additionally, AbstractViewElement provides {@link #byInner(com.redhat.darcy.ui.api.Locator)}
 * for conveniently nesting elements underneath some other locator or element. For example:
 *
 * <code><pre>
 *     public class MyCustomElement extends AbstractViewElement {
 *         private TextInput input = textInput(byInner(By.id("input"));
 *
 *         // snip
 *     }
 * </pre></code>
 *
 * @see #byInner(com.redhat.darcy.ui.api.Locator)
 * @see com.redhat.darcy.ui.AbstractView
 * @see com.redhat.darcy.ui.api.ViewElement
 */
public abstract class AbstractViewElement extends AbstractView implements ViewElement {
    /**
     * The parent element of this ViewElement, should one be present.
     *
     * <p>If your ViewElement is nested under some locator, but there is no specific element at that
     * locator, then this parent element will never be present.
     */
    protected final Element parent;

    /**
     * @see #byInner(com.redhat.darcy.ui.api.Locator)
     */
    private final UnaryOperator<Locator> makeInnerLocator;

    /**
     * Creates a nested View underneath some parent Locator.
     *
     * @see #byInner(com.redhat.darcy.ui.api.Locator)
     */
    public AbstractViewElement(Locator parent) {
        // How to specify element type? Is this ever going to be needed?
        this(Elements.element(parent), l -> By.chained(parent, l));
    }

    /**
     * Creates a nested View underneath some parent element, which is necessary for creating a list
     * of this ViewElement from some {@link com.redhat.darcy.ui.api.Locator}, as the locator itself
     * may correspond to a number of elements, so each unique element found by the same locator is
     * used to construct this ViewElement to ensure each represents a unique element.
     *
     * @see #byInner(com.redhat.darcy.ui.api.Locator)
     */
    public AbstractViewElement(Element parent) {
        this(parent, l -> By.nested(parent, l));
    }

    private AbstractViewElement(Element parent, UnaryOperator<Locator> makeInnerLocator) {
        this.parent = parent;
        this.makeInnerLocator = makeInnerLocator;
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
     * Transforms a locator to its equivalent but nested under the parent element or chained with
     * the parent locator for this ViewElement. This will choose whether to chain a locator or nest
     * under an element reference based on the constructor used.
     *
     * <p>For example, if the locator constructor was used with a {@code By.id("parent")}, calling
     * {@code byInner(By.id("inside"))} would return a
     * {@link com.redhat.darcy.ui.By.ByChained} with {@code By.id("parent"), By.id("inside")}.
     *
     * <p>If the element constructor was used, calling {@code byInner(By.id("inside"))} would return
     * a {@link com.redhat.darcy.ui.By.ByNested} with the parent element passed in the constructor,
     * and the {@code By.id("inside")}.
     *
     * @see #AbstractViewElement(com.redhat.darcy.ui.api.elements.Element)
     * @see #AbstractViewElement(com.redhat.darcy.ui.api.Locator)
     */
    protected final Locator byInner(Locator locator) {
        return makeInnerLocator.apply(locator);
    }
}
