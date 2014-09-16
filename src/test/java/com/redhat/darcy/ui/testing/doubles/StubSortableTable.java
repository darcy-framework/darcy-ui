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

package com.redhat.darcy.ui.testing.doubles;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.elements.SortableTable;

public class StubSortableTable implements SortableTable<StubSortableTable> {
    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("getRowCount");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("isEmpty");
    }

    @Override
    public boolean isDisplayed() {
        throw new UnsupportedOperationException("isDisplayed");
    }

    @Override
    public boolean isPresent() {
        throw new UnsupportedOperationException("isPresent");
    }

    @Override
    public void setContext(ElementContext context) {
        throw new UnsupportedOperationException("setContext");
    }

    @Override
    public ElementContext getContext() {
        throw new UnsupportedOperationException("getContext");
    }

    @Override
    public boolean isLoaded() {
        throw new UnsupportedOperationException("isLoaded");
    }
}
