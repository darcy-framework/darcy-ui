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
 * A locator is a strategy for finding an instance (or List of instances) that implement some class,
 * given some {@link Context}.
 */
public interface Locator {
    /**
     * Returns a list of all the elements found by this locator, of the given type, in the given
     * context.
     * 
     * @param type
     * @param context
     * @return
     */
    <T> List<T> findAll(Class<T> type, Context context);
    
    /**
     * Returns one element found by this locator, of the given type, in the given context.
     * <P>
     * By default, if there are multiple instances that would satisfy this locator, then this method 
     * picks one of them. The instance picked is up to implementation.
     * 
     * @param type
     * @param context
     * @throws com.redhat.darcy.ui.NotFoundException if the element cannot be found.
     * @return
     */
    default <T> T find(Class<T> type, Context context) {
        List<T> found = findAll(type, context);
        
        if (found.isEmpty()) {
            throw new NotFoundException(type, this);
        }
        
        return found.get(0);
    }
}
