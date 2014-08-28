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

import javax.annotation.Nullable;

/**
 * A role interface for tables which allow sorting ascending or descending by column.
 */
public interface SortableTable<T extends SortableTable<T>> extends Table<T> {
    @SuppressWarnings("unchecked")
    default void sort(SortableColumnDefinition<T, ?> column, SortDirection direction) {
        column.sort((T) this, direction);
    }

    /**
     * @return Null if the column is not used for sorting.
     */
    @SuppressWarnings("unchecked")
    default @Nullable SortDirection getSortDirection(SortableColumnDefinition<T, ?> column) {
        return column.getSortDirection((T) this);
    }

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.Column} for this table.
     */
    @SuppressWarnings("unchecked")
    default <U> SortableColumn<ColumnDefinition<T, U>, T, U> getColumn(SortableColumnDefinition<T, U> column) {
        return new SortableColumn<>((T) this, column);
    }

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.HeadedColumn} for this
     * table.
     */
    @SuppressWarnings("unchecked")
    default <U, E> SortableHeadedColumn<SortableColumnWithHeaderDefinition<T, U, E>, T, U, E> getColumn(
            SortableColumnWithHeaderDefinition<T, U, E> column) {
        return new SortableHeadedColumn<>((T) this, column);
    }

    interface SortableColumnDefinition<T extends Table<T>, U> extends ColumnDefinition<T, U> {
        void sort(T table, SortDirection direction);

        /**
         * @return Null if the column is not used for sorting.
         */
        @Nullable SortDirection getSortDirection(T table);
    }

    interface SortableColumnWithHeaderDefinition<T extends Table<T>, U, E>
            extends ColumnWithHeaderDefinition<T, U, E>, SortableColumnDefinition<T, U> {}

    /**
     * Represents a concrete column within a specific table instance. Given this and a row index,
     * you will be able to look up the contents of a specific cell.
     *
     * @param <T> The type of the definition for this column.
     * @param <U> The type of table this column is within.
     * @param <E> The type of the cell content within this column.
     */
    final class SortableColumn<T extends ColumnDefinition<U, E>, U extends Table<U>, E> {
        private final U table;
        private final T column;

        public SortableColumn(U table, T column) {
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

    final class SortableHeadedColumn<T extends SortableColumnDefinition<U, E>
            & HeaderDefinition<U, V>, U extends Table<U>, E, V> {
        private final U table;
        private final T column;

        public SortableHeadedColumn(U table, T column) {
            this.table = table;
            this.column = column;
        }

        public void sort(SortDirection direction) {
            column.sort(table, direction);
        }

        public @Nullable SortDirection getSortDirection() {
            return column.getSortDirection(table);
        }

        public V getHeader() {
            return column.getHeader(table);
        }

        public E getCell(int row) {
            return column.getCell(table, row);
        }

        public U getTable() {
            return table;
        }
    }

    enum SortDirection {
        ASCENDING,
        DESCENDING
    }
}
