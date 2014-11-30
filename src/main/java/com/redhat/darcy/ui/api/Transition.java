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
 * Fluently creates {@link TransitionEvent}s for a given destination {@link com.redhat.darcy.ui.api.View}.
 */
public interface Transition {
    /**
     * Create a {@link TransitionEvent} that will wait for a transition to the destination 
     * {@link com.redhat.darcy.ui.api.View} to complete.
     * @param view
     * @return
     */
    <T extends View> TransitionEvent<T> to(T view);
}
