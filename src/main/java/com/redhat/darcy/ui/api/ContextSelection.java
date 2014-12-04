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

package com.redhat.darcy.ui.api;

import com.redhat.darcy.ui.api.elements.Findable;

import java.util.List;

/**
 * Fluently retrieve a context reference or a context list reference of a specified type.
 * @see ParentContext
 */
public interface ContextSelection extends Selection {
    /**
     * Retrieve a reference to another context found by the current. This context may be a "child"
     * or a "sibling," this is up to the implementation.
     */
    <T extends Context & Findable> T contextOfType(Class<T> contextType, Locator locator);
    
    /**
     * Retrieve a reference to a list of other context found by the current. These contexts may be 
     * "children" or "siblings," this is up to the implementation.
     */
    <T extends Context & Findable> List<T> contextsOfType(Class<T> contextType, Locator locator);

    default FindableElementContext elementContext(Locator locator) {
        return contextOfType(FindableElementContext.class, locator);
    }

    default List<FindableElementContext> elementContexts(Locator locator) {
        return contextsOfType(FindableElementContext.class, locator);
    }

    default FindableParentContext parentContext(Locator locator) {
        return contextOfType(FindableParentContext.class, locator);
    }

    default List<FindableParentContext> parentContexts(Locator locator) {
        return contextsOfType(FindableParentContext.class, locator);
    }
}
