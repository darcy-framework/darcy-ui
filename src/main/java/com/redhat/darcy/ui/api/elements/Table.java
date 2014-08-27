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

import com.redhat.darcy.ui.api.ViewElement;

import org.hamcrest.Matcher;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Table<T extends Table<T>> extends ViewElement {

    int getRowCount();

    boolean isEmpty();

    default Iterable<Row<T>> rows() {
        return () -> new Iterator<Row<T>>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor + 1 <= getRowCount();
            }

            @SuppressWarnings("unchecked")
            @Override
            public Row<T> next() {
                return new Row<T>((T) Table.this, ++cursor);
            }
        };
    }

    @SuppressWarnings("unchecked")
    default Row<T> getRow(int row) {
        return new Row<>((T) this, row);
    }

    @SuppressWarnings("unchecked")
    default <U> Column<ColumnDefinition<T, U>, T, U> getColumn(ColumnDefinition<T, U> column) {
        return new Column<>((T) this, column);
    }

    @SuppressWarnings("unchecked")
    default <U> U getCell(ColumnDefinition<T, U> column, int row) {
        return column.getCell((T) this, row);
    }

    default <U> Stream<Row<T>> getRowsWhere(ColumnDefinition<T, U> column, Predicate<? super U> predicate) {
        return StreamSupport.stream(rows().spliterator(), false)
                .filter(r -> predicate.test(r.getCell(column)));
    }

    default <U> Stream<Row<T>> getRowsWhere(ColumnDefinition<T, U> column, Matcher<? super U> matcher) {
        return getRowsWhere(column, matcher::matches);
    }

    default <U> Stream<U> getCellsWhere(ColumnDefinition<T, U> column, Predicate<? super U> predicate) {
        return getRowsWhere(column, predicate)
                .map(r -> r.getCell(column));
    }

    default <U> Stream<U> getCellsWhere(ColumnDefinition<T, U> column, Matcher<? super U> matcher) {
        return getCellsWhere(column, matcher::matches);
    }

    interface ColumnDefinition<T extends Table<T>, U> {
        U getCell(T table, int row);
    }

    final class Column<T extends ColumnDefinition<U, E>, U extends Table<U>, E> {
        private final U table;
        private final T column;

        public Column(U table, T column) {
            this.table = table;
            this.column = column;
        }

        public E getCell(int row) {
            return column.getCell(table, row);
        }

        public U getTable() {
            return table;
        }
    }

    final class Row<T extends Table<T>> {
        private final T table;
        private final int index;

        public Row(T table, int index) {
            this.table = table;
            this.index = index;
        }

        public <U> U getCell(ColumnDefinition<T, U> column) {
            return column.getCell(getTable(), getIndex());
        }

        public T getTable() {
            return table;
        }

        public int getIndex() {
            return index;
        }
    }
}
