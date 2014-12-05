/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-web.

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

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.ui.api.elements.HasAttributes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

public class IdOfHandler implements InvocationHandler {
    private final Context context;
    private final Class type;
    private final Findable original;

    private Findable usingId;

    public IdOfHandler(Locator locator, Class type, Context context) {
        this.context = context;
        this.type = type;

        original = locator.find(type, context);

        if (!(original instanceof HasAttributes)) {
            throw new DarcyException("Cannot lookup an id for a Findable if it does not implement "
                    + "HasAttributes. Findable was, " + original);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (usingId == null && original.isPresent()) {
            Optional<String> id = getId((HasAttributes) original);

            if (id.isPresent()) {
                usingId = By.id(id.get()).find(type, context);
            }
        }

        if ("getWrappedElement".equals(method.getName())) {
            return findable();
        }

        return method.invoke(findable(), args);
    }

    private Optional<String> getId(HasAttributes hasAttributes) {
        String id = hasAttributes.getAttribute("id");

        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(id);
    }

    private Findable findable() {
        return (usingId == null) ? original : usingId;
    }
}
