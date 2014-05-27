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

import java.util.List;

/**
 * @see ParentContext
 */
public interface ContextSelection {
    /**
     * Retrieve a reference to another context found by the current. This context may be a "child"
     * or a "sibling," this is up to the implementation.
     * @param contextType
     * @param locator
     * @return
     */
    <T extends Context> T ofType(Class<T> contextType, Locator locator);
    
    /**
     * Retrieve a reference to a list of other context found by the current. These contexts may be 
     * "children" or "siblings," this is up to the implementation.
     * @param contextType
     * @param locator
     * @return
     */
    <T extends Context> List<T> listOfType(Class<T> contextType, Locator locator);
}
