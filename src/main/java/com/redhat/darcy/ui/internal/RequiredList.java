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

package com.redhat.darcy.ui.internal;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;

public class RequiredList<T> {
    private final List<T> list;
    private final RequiredListBounds bounds;
    private final Class<?> genericType;

    @SuppressWarnings("unchecked")
    public RequiredList(Field field, Object in) {
        this.genericType = ReflectionUtil.getGenericTypeOfCollectionField(field);
        this.bounds = new RequiredListBounds(field);

        try {
            this.list = (List<T>) field.get(in);
        } catch (IllegalAccessException iae) {
            throw new DarcyException("Couldn't access value of object", iae);
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Can not cast field of object to List");
        }
    }

    public List<T> list() {
        return list;
    }

    public Class<?> genericType() {
        return genericType;
    }

    public int atMost() {
        return bounds.atMost();
    }

    public int atLeast() {
        return bounds.atLeast();
    }
}
