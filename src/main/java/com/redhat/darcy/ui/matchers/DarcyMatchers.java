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

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.ui.api.elements.Requireable;
import com.redhat.darcy.ui.api.elements.Text;

import org.hamcrest.Matcher;

public abstract class DarcyMatchers {
    public static <T extends Element> Matcher<T> displayed() {
        return new ElementIsDisplayed<>();
    }

    public static <T extends Findable> Matcher<T> present() {
        return new FindableIsPresent<>();
    }

    public static <T extends Requireable> Matcher<T> required() {
        return new RequireableIsRequired<>();
    }
    
    public static <T extends Requireable> Matcher<T> notRequired() {
        return new RequireableIsNotRequired<>();
    }
    
    public static <T extends View> Matcher<T> loaded() {
        return new ViewIsLoaded<T>();
    }

    public static <T extends View> Matcher<T> loadedInContext(ElementContext context) {
        return new ViewIsLoadedInContext<T>(context);
    }
    
    public static <T extends Text> Matcher<T> elementText(Matcher<? super String> matcher) {
        return new ElementText<>(matcher);
    }
    
    public static <T extends Text> Matcher<T> visibleText(Matcher<? super String> matcher) {
        return new VisibleText<>(matcher);
    }
    
}
