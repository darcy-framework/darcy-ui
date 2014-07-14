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

package com.redhat.darcy.ui.api.elements;

/**
 * Represents any "Findable" UI type. A type is "Findable" when it has to be searched for, and that
 * search may or may not be successful. The convention in darcy for Findable types is that actually
 * being found does not necessarily predate their instantiation or existence. A "find" method may
 * return a Findable which is not actually there. This is expressed in the documentation for
 * {@link com.redhat.darcy.ui.api.Selection} as well as {@link com.redhat.darcy.ui.api.Context#find()}.
 * <p>
 * To test for the presence of a Findable, use {@link #isPresent()}.
 */
public interface Findable {
    /**
     * Tests for the presence of the {@link com.redhat.darcy.ui.api.elements.Findable}. A type that is
     * not present will throw an exception when attempting an action on that type which requires
     * its presence.
     */
    boolean isPresent();
}
