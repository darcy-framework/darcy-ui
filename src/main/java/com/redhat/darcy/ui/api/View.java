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

/**
 * A View is the fundamental UI modeling type. It is some collection of elements and conditions,
 * typically with public methods that describe the business actions available on that View (like
 * "login" or "createNewUser"), as well as any necessary finer-grain user-input interactions such as
 * "typeLogin" and "typePassword" that the higher level functions might use.
 * <P>
 * A View is almost 1:1 with the concept of a page, except that you can define a View that is also a
 * subset of a page, like say a menu that using the same shared code, at the same shared location,
 * among several pages. Views can then include other Views and expose them via public getters or
 * forward their methods.
 * 
 * @see com.redhat.darcy.ui.AbstractView
 */
public interface View extends HasElementContext {
    /**
     * Sets the context for the current View, and also initializes the View should the
     * implementation require.
     */
    @Override
    void setContext(ElementContext context);
    
    ElementContext getContext();
    
    /**
     * Accurately determining whether or not a View is loaded is critical to synchronizing your
     * automation code with a user interface. Return true if this View, and only this View, is
     * loaded, which is usually reliably determined by checked for the visible of the specific
     * configuration of elements that make up this View.
     *
     * <p>{@link com.redhat.darcy.ui.AbstractView} simplifies this determination by allowing the use
     * of annotations to configure how to determine whether or not this View is loaded.
     *
     * @see com.redhat.darcy.ui.AbstractView
     */
    boolean isLoaded();
}
