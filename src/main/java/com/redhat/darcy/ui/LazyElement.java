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

package com.redhat.darcy.ui;

/**
 * Interface for element fields created statically and assigned an ElementContext to look in
 * by {@link com.redhat.darcy.ui.AbstractView}. Real implementations must be found via a locator and
 * a context. A context is associated with a View at runtime, so these element fields, when created,
 * have no context to refer to. Thus, they must be created as proxies, which delay looking up the
 * actual element until the element is used. The context is assigned via this interface and {@code
 * AbstractView}.
 */
public interface LazyElement extends HasElementContext {
}
