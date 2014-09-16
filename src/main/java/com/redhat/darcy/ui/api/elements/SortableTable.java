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
import java.util.Objects;

/**
 * A role interface for tables which allow sorting ascending or descending by column.
 */
public interface SortableTable<T extends SortableTable<T>> extends Table<T> {
    @SuppressWarnings("unchecked")
    default void sort(SortableColumn<T, ?> column, SortDirection direction) {
        column.sort((T) this, direction);
    }

    /**
     * @return Null if the column is not used for sorting.
     */
    @SuppressWarnings("unchecked")
    default @Nullable SortDirection getSortDirection(SortableColumn<T, ?> column) {
        return column.getSortDirection((T) this);
    }

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.TableColumn} for this table.
     */
    @SuppressWarnings("unchecked")
    default <U> SortableTableColumn<SortableColumn<T, U>, T, U> getColumn(SortableColumn<T, U> column) {
        return new SortableTableColumn((T) this, column);
    }

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.HeadedTableColumn} for this
     * table.
     */
    @SuppressWarnings("unchecked")
    default <U, E> SortableHeadedTableColumn<SortableColumnWithHeader<T, U, E>, T, U, E> getColumn(
            SortableColumnWithHeader<T, U, E> column) {
        return new SortableHeadedTableColumn<>((T) this, column);
    }

    interface SortableColumn<T extends Table<T>, U> extends Column<T, U> {
        void sort(T table, SortDirection direction);

        /**
         * @return Null if the column is not used for sorting.
         */
        @Nullable SortDirection getSortDirection(T table);
    }

    interface SortableColumnWithHeader<T extends Table<T>, U, E>
            extends ColumnWithHeader<T, U, E>, SortableColumn<T, U> {}

    /**
     * Represents a concrete column within a specific table instance. Given this and a row index,
     * you will be able to look up the contents of a specific cell.
     *
     * @param <T> The type of the definition for this column.
     * @param <U> The type of table this column is within.
     * @param <E> The type of the cell content within this column.
     */
    final class SortableTableColumn<T extends SortableColumn<U, E>, U extends Table<U>, E> {
        private final U table;
        private final T column;

        public SortableTableColumn(U table, T column) {
            this.table = Objects.requireNonNull(table, "table");
            this.column = Objects.requireNonNull(column, "column");
        }

        public void sort(SortDirection direction) {
            column.sort(table, direction);
        }

        public @Nullable SortDirection getSortDirection() {
            return column.getSortDirection(table);
        }

        public E getCell(int rowIndex) {
            return column.getCell(table, rowIndex);
        }

        public U getTable() {
            return table;
        }

        @Override
        public String toString() {
            return "SortableTableColumn: {table: " + table + ", column: " + column + "}";
        }

        @Override
        public int hashCode() {
            return Objects.hash(table, column);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof SortableTableColumn)) {
                return false;
            }

            SortableTableColumn other = (SortableTableColumn) o;

            return other.table.equals(table) && other.column.equals(column);
        }
    }

    final class SortableHeadedTableColumn<T extends SortableColumn<U, E> & Header<U, V>,
            U extends Table<U>, E, V> {
        private final U table;
        private final T column;

        public SortableHeadedTableColumn(U table, T column) {
            this.table = Objects.requireNonNull(table, "table");
            this.column = Objects.requireNonNull(column, "column");
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

        public E getCell(int rowIndex) {
            return column.getCell(table, rowIndex);
        }

        public U getTable() {
            return table;
        }

        @Override
        public String toString() {
            return "SortableHeadedTableColumn: {table: " + table + ", column: " + column + "}";
        }

        @Override
        public int hashCode() {
            return Objects.hash(table, column);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof SortableHeadedTableColumn)) {
                return false;
            }

            SortableHeadedTableColumn other = (SortableHeadedTableColumn) o;

            return other.table.equals(table) && other.column.equals(column);
        }
    }

    enum SortDirection {
        ASCENDING,
        DESCENDING
    }
}
