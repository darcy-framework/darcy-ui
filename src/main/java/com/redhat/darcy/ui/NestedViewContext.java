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

public class NestedViewContext implements ViewContext {
    private final View view;
    private final By parentBy;
    
    /**
     * 
     * @param view The parent View.
     */
    public NestedViewContext(View view) {
        this(view, null);
    }
    
    /**
     * 
     * @param view The parent view.
     * @param parentBy Can be null if this nested View does not have any "root" element.
     */
    public NestedViewContext(View view, By parentBy) {
        this.view = view;
        this.parentBy = parentBy;
    }

    @Override
    public <T extends Element> T findElement(Class<T> type, By locator) {
        return view.getContext().findElement(type, nestedLocator(locator));
    }

    @Override
    public ViewContext findContext(By locator) {
        return view.getContext().findContext(locator);
    }
    
    // This could be pulled into parent class or interface
    public View getParentView() {
        return view;
    }
    
    // This is implementation specific (ie other impl would be Element as opposed to By
    public By getParentLocator() {
        return parentBy;
    }
    
    private By nestedLocator(By locator) {
        if (parentBy == null) {
            return locator;
        } else {
            return By.chained(parentBy, locator);
        }
    }
}
