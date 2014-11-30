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

package com.redhat.darcy.ui.matchers;

import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.redhat.darcy.ui.matchers.DarcyMatchers.displayed;
import static com.redhat.darcy.ui.matchers.DarcyMatchers.loaded;
import static com.redhat.darcy.ui.matchers.DarcyMatchers.present;

/**
 * Tests whether a required field's object's state is appropriate for a view with those fields to be
 * considered loaded. What is required of an object's state is dependent on the type of object.
 * See {@link #doesItemMatchAppropriateCondition(Object)} for details.
 * <p>
 * If the tested object is not a {@link com.redhat.darcy.ui.api.elements.Findable},
 * {@link com.redhat.darcy.ui.api.elements.Element}, or {@link com.redhat.darcy.ui.api.View}, then
 * it will never match.
 */
public class LoadConditionMatcher extends BaseMatcher<Object> {
    @Override
    public boolean matches(Object item) {
        return doesItemMatchAppropriateCondition(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Findable, Element, or View that is present, displayed, or loaded "
                + "respectively, depending on the type.");
    }

    /**
     * Takes an object, and determines a condition for that object that should satisfy the
     * containing view is loaded. Different conditions are made depending on the type of object.
     *
     * <table>
     *     <thead>
     *         <tr>
     *             <td>Type</td>
     *             <td>Method</td>
     *         </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>{@link com.redhat.darcy.ui.api.View}</td>
     *             <td>{@link com.redhat.darcy.ui.api.View#isLoaded()}</td>
     *         </tr>
     *         <tr>
     *             <td>{@link com.redhat.darcy.ui.api.elements.Element}</td>
     *             <td>{@link com.redhat.darcy.ui.api.elements.Element#isDisplayed()}</td>
     *         </tr>
     *         <tr>
     *             <td>{@link com.redhat.darcy.ui.api.elements.Findable}</td>
     *             <td>{@link com.redhat.darcy.ui.api.elements.Findable#isPresent()}</td>
     *         </tr>
     *     </tbody>
     * </table>
     */
    private boolean doesItemMatchAppropriateCondition(Object item) {
        Matcher<?> matcher;

        if (item instanceof View) {
            matcher = loaded();
        } else if (item instanceof Element) {
            matcher = displayed();
        } else {
            matcher = present();
        }

        return matcher.matches(item);
    }
}
