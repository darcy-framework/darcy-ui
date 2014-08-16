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

package com.redhat.darcy.ui.api.elements;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.ElementSelection;
import com.redhat.darcy.ui.api.Selection;

import org.hamcrest.Matcher;

public interface Table<T> {
    TableRow getRow(int row);
    <U extends T, E> TableColumn<E> getColumn(U column);
    <E> E getCell(T column, int row);
    <E> E getCell(T column, Matcher<? super E> matcher);

    class ColumnA implements TableColumn<String> {

        @Override
        public String getCell(int row) {
            return find().text(byInner(By.xpath))
        }
    }

    class Columns {
        static TableColumn<String> COLUMN_A = new ColumnA();
    }
}
