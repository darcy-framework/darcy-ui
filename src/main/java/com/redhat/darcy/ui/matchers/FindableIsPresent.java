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

import com.redhat.darcy.ui.api.elements.Findable;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class FindableIsPresent<T extends Findable> extends TypeSafeMatcher<T> {
    @Override
    public boolean matchesSafely(T findable) {
        return findable.isPresent();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Findable that is present");
    }
    
}
