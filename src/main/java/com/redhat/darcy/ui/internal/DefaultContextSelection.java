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

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.ContextSelection;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.ui.api.elements.Findable;

import java.util.List;

/**
 * A simple default {@link com.redhat.darcy.ui.api.ContextSelection} that forwards to {@link com.redhat.darcy.ui.api.Locator#find(Class, com.redhat.darcy.ui.api.Context)}
 * and {@link com.redhat.darcy.ui.api.Locator#findAll(Class, com.redhat.darcy.ui.api.Context)} respectively.
 */
public class DefaultContextSelection implements ContextSelection {
    private final ParentContext parentContext;
    
    public DefaultContextSelection(ParentContext parentContext) {
        this.parentContext = parentContext;
    }
    
    @Override
    public <T extends Context & Findable> T contextOfType(Class<T> contextType, Locator locator) {
        return locator.find(contextType, parentContext);
    }
    
    @Override
    public <T extends Context & Findable> List<T> contextsOfType(Class<T> contextType, Locator locator) {
        return locator.findAll(contextType, parentContext);
    }
    
}
