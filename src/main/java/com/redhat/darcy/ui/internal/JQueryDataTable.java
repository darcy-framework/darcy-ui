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

import static com.redhat.darcy.ui.Elements.label;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.annotations.InheritsContext;
import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Label;

import java.util.List;

public class JQueryDataTable extends AbstractViewElement {
    public static JQueryDataTable jQueryDataTable(Locator locator) {
        return new @InheritsContext JQueryDataTable(locator);
    }

    public static List<JQueryDataTable> jQueryDataTables(Locator locator) {
        return new @InheritsContext ViewElementList<>(JQueryDataTable::new, locator);
    }

    @Require
    private Label info = label(nested(By.id("info")));

    public JQueryDataTable(Locator parent) {
        super(parent);
    }

    public JQueryDataTable(Element parent) {
        super(parent);
    }

    public Element cell(int row, int column) {
        return getContext().find().element(nested(By.xpath("")));
    }
}
