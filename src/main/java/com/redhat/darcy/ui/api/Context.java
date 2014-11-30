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
 * A marker interface for a class that can find "stuff." The contract of implementing this interface
 * (or one of its subclasses) is that you also implement specific means of finding that stuff, that
 * corresponds to {@link Locator}s (for instance, {@link com.redhat.darcy.ui.internal.FindsById} which is used by {@link
 * com.redhat.darcy.ui.By.ById}).
 */
public interface Context {
    /**
     * Begin to fluently retrieve a reference to a UI object of some type (as determined by the type
     * of Context), with some {@link Locator} in order to find that object.
     * <p>
     * It is important to note that retrieving an object from this method will not throw an
     * exception or return null if no such object can be found with the given locator. If you wish
     * to determine if an object is actually found with the {@link Locator}
     * used, it is up to the object's API to, as it should, define some kind of
     * <code>isPresent</code> method to determine this. Otherwise, attempting to do something with
     * that object that requires its presence and/or visibility <em>will</em> throw an exception.
     *
     * @return
     */
    Selection find();
}
