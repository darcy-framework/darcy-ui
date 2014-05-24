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

/**
 * A marker interface for a class that can find "stuff." The contract of implementing this interface
 * (or one of its subclasses) is that you also implement, or forward to some type that does 
 * implement, specific means of finding that stuff, that corresponds to {@link Locator}s (for 
 * instance, {@link FindsById}).
 */
public interface Context {
    
}
