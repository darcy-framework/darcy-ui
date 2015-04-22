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

import com.redhat.synq.Event;

/**
 * A TransitionEvent is some awaitable {@link com.redhat.synq.Event} that marks the completed 
 * transition from one {@link com.redhat.darcy.ui.api.View} to another within the same
 * {@link com.redhat.darcy.ui.api.ElementContext}.
 * <P>
 * It can create other, similar Events if we expect this View to be loaded in a different context.
 * @param <T> The type of View to transition to.
 */
public interface TransitionEvent<T extends View> extends Event<T> {
    Event<T> inNewContext();
    Event<T> inNewContext(Locator locator);
}
