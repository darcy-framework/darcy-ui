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

import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SORTED;

import com.redhat.darcy.ui.api.ViewElement;

import org.hamcrest.Matcher;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Tables are constructed of rows and columns. Cells can contain any arbitrary configuration of
 * elements, and it is assumed that every row in a column has a consistent element configuration (or
 * at least consistent logic to determine what is inside a cell within that column given a
 * particular row). Given this, the general "equation" of this interface and related interfaces is,
 * "table + row index + column = content of a cell", which should intuitively make sense.
 *
 * <p>You can see this expressed in the interaction between each interface, specifically
 * {@link com.redhat.darcy.ui.api.elements.Table.Column}, which may also be thought of as
 * a function which accepts a table and a row index as an argument, and spits out the contents of
 * the cell within this column at the passed row index. The contents of the cells in that column are
 * arbitrary and may be typed whatever is appropriate for that table. Many column definitions will
 * return Strings or other element types, such as Checkboxes or Links.
 *
 * <p>Knowledge about how to retrieve particular content is contained with column implementations.
 * This makes sense: columns are typed by the content of cells in that column. If one column has
 * only text in it, and another has checkboxes, those are clearly different column implementations.
 * This means that defining a table within your View is not merely about implementing this
 * interface, but also necessarily implementing some
 * {@link com.redhat.darcy.ui.api.elements.Table.Column}s. Otherwise, no content in your table would
 * actually be retrievable. It is common, then, to implement table (whether directly or by extending
 * a base class), for each different table that would appear in your view. Then, within that class,
 * have appropriately visible instances of {@link com.redhat.darcy.ui.api.elements.Table.Column}
 * that can be used in cooperation with an instance of that table.
 *
 * <p>Your instinct may be to consider a table an element, however tables are really
 * {@link com.redhat.darcy.ui.api.ViewElement ViewElements}&mdash;they may behave as a
 * single element since they are found by a locator within some element context, however they are
 * themselves containers of many more elements (their cells and their cells' contents), which means
 * they are also views.
 *
 * @param <T> The type of the subclass of this table. Allows the parent interface to refer to the
 * subclass's specific type.
 */
public interface Table<T extends Table<T>> extends ViewElement {

    /**
     * The currently visible number of rows shown within the table.
     */
    int getRowCount();

    /**
     * Equivalent to {@link #getRowCount()} == 0, however from an implementation point of view,
     * different tables often have special indications for when there is no data to display within
     * the table.
     */
    boolean isEmpty();

    /**
     * An iterable for all of the currently visible rows, which means the amount of items in the
     * iterable should match {@link #getRowCount()}.
     *
     * <p>The iterable iterates over {@link com.redhat.darcy.ui.api.elements.Table.Row} objects that
     * are tied to this table.
     */
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

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.Row} for this table.
     */
    @SuppressWarnings("unchecked")
    default Row<T> getRow(int rowIndex) {
        return new Row<>((T) this, rowIndex);
    }

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.TableColumn} for this table.
     */
    @SuppressWarnings("unchecked")
    default <U> TableColumn<Column<T, U>, T, U> getColumn(Column<T, U> column) {
        return new TableColumn<>((T) this, column);
    }

    /**
     * @return A specific {@link com.redhat.darcy.ui.api.elements.Table.HeadedTableColumn} for this
     * table.
     */
    @SuppressWarnings("unchecked")
    default <U, E> HeadedTableColumn<ColumnWithHeader<T, U, E>, T, U, E> getColumn(
            ColumnWithHeader<T, U, E> column) {
        return new HeadedTableColumn<>((T) this, column);
    }

    /**
     * @return A specific cell's contents within this table, as determined by the specified
     * {@link com.redhat.darcy.ui.api.elements.Table.Column} and row index.
     */
    @SuppressWarnings("unchecked")
    default <U> U getCell(Column<T, U> column, int rowIndex) {
        return column.getCell((T) this, rowIndex);
    }

    /**
     * @return header A specific column's header's contents within this table, as determined by the
     * specified {@link com.redhat.darcy.ui.api.elements.Table.Header}.
     * @param <U> The type of contents within the header.
     */
    @SuppressWarnings("unchecked")
    default <U> U getHeader(Header<T, U> header) {
        return header.getHeader((T) this);
    }

    /**
     * @return A filtered {@link java.util.stream.Stream} of the rows in this table where the
     * contents of a particular column match the specified {@link java.util.function.Predicate}.
     */
    default <U> Stream<Row<T>> getRowsWhere(Column<T, U> column, Predicate<? super U> predicate) {
        return StreamSupport.stream(
                Spliterators.spliterator(rows().iterator(), getRowCount(),
                        DISTINCT | ORDERED | SORTED | NONNULL), false)
                .filter(r -> predicate.test(r.getCell(column)));
    }

    /**
     * @return The first filtered {@link java.util.Optional} of the rows in this table where the
     * contents of a particular column match the specified {@link java.util.function.Predicate}.
     */
    default <U> Optional<Row<T>> getFirstRowWhere(Column<T, U> column, Predicate<? super U> predicate) {
        return getRowsWhere(column, predicate).findFirst();
    }

    /**
     * @return A filtered {@link java.util.stream.Stream} of the rows in this table where the
     * contents of a particular column match the specified Hamcrest {@link org.hamcrest.Matcher}.
     */
    default <U> Stream<Row<T>> getRowsWhere(Column<T, U> column, Matcher<? super U> matcher) {
        return getRowsWhere(column, matcher::matches);
    }

    /**
     * @return The first filtered {@link java.util.Optional} of the rows in this table where the
     * contents of a particular column match the specified Hamcrest {@link org.hamcrest.Matcher}.
     */
    default <U> Optional<Row<T>> getFirstRowWhere(Column<T, U> column, Matcher<? super U> matcher) {
        return getRowsWhere(column, matcher::matches).findFirst();
    }

    /**
     * @return Like
     * {@link #getRowsWhere(com.redhat.darcy.ui.api.elements.Table.Column, java.util.function.Predicate)},
     * except instead of returning a stream of rows, returns a stream of the exact cells that
     * matched the specified predicate within the specified column. Useful if you want the contents
     * of cells that match some range of possible values, and you would like to inspect the actual
     * values.
     */
    default <U> Stream<U> getCellsWhere(Column<T, U> column, Predicate<? super U> predicate) {
        return getRowsWhere(column, predicate)
                .map(r -> r.getCell(column));
    }

    /**
     * @return Like
     * {@link #getRowsWhere(com.redhat.darcy.ui.api.elements.Table.Column, org.hamcrest.Matcher)},
     * except instead of returning a stream of rows, returns a stream of the exact cells that
     * matched the specified matcher within the specified column. Useful if you want the contents
     * of cells that match some range of possible values, and you would like to inspect the actual
     * values.
     */
    default <U> Stream<U> getCellsWhere(Column<T, U> column, Matcher<? super U> matcher) {
        return getCellsWhere(column, matcher::matches);
    }

    /**
     * A column definition does the actual work of returning some useful contents within the table,
     * limited to a particular column. Each column is expected to have consistent cell contents. If
     * these contents are variable, consider returning instances of a custom class which can handle
     * the various contents, or implementing different column definitions, despite actually
     * referring to the same visual column.
     *
     * @param <T> The class of table with which this column refers to. This will be your specific
     * table implementation, or one of the common ones provided in implementations of darcy-ui.
     * @param <U> The class that models the contents within cells of this column.
     */
    interface Column<T extends Table<T>, U> {
        U getCell(T table, int rowIndex);
    }

    /**
     * Like a {@link com.redhat.darcy.ui.api.elements.Table.Column}, but contains the
     * knowledge for locating the header of a particular column. Typically, a column definition
     * will implement {@link com.redhat.darcy.ui.api.elements.Table.Column} alone at the
     * very least, and if that column has a header, additionally implement
     * {@link com.redhat.darcy.ui.api.elements.Table.Header}.
     *
     * @param <T> The class of table with which this header refers to. This will be your specific
     * table implementation, or one of the common ones provided in implementations of darcy-ui.
     * @param <U> The class that models the contents within the header for this column.
     */
    interface Header<T extends Table<T>, U> {
        U getHeader(T table);
    }

    interface ColumnWithHeader<T extends Table<T>, U, E> extends Column<T, U>,
            Header<T, E> {}

    /**
     * Represents a concrete column within a specific table instance. Given this and a row index,
     * you will be able to look up the contents of a specific cell.
     *
     * @param <T> The type of the definition for this column.
     * @param <U> The type of table this column is within.
     * @param <E> The type of the cell content within this column.
     */
    final class TableColumn<T extends Column<U, E>, U extends Table<U>, E> {
        private final U table;
        private final T column;

        public TableColumn(U table, T column) {
            this.table = Objects.requireNonNull(table, "table");
            this.column = Objects.requireNonNull(column, "column");
        }

        public E getCell(int rowIndex) {
            return column.getCell(table, rowIndex);
        }

        public U getTable() {
            return table;
        }

        @Override
        public String toString() {
            return "TableColumn: {table: " + table + ", column: " + column + "}";
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

            if (!(o instanceof TableColumn)) {
                return false;
            }

            TableColumn other = (TableColumn) o;

            return other.table.equals(table) && other.column.equals(column);
        }
    }

    /**
     * Represents a concrete column within a specific table instance. Given this and a row index,
     * you will be able to look up the contents of a specific cell.
     *
     * <p>This column also contains a header of some type.
     *
     * @param <T> The type of the definition for this column.
     * @param <U> The type of table this column is within.
     * @param <E> The type of the cell content within this column.
     */
    final class HeadedTableColumn<T extends Column<U, E> & Header<U, V>,
            U extends Table<U>, E, V> {
        private final U table;
        private final T column;

        public HeadedTableColumn(U table, T column) {
            this.table = Objects.requireNonNull(table, "table");
            this.column = Objects.requireNonNull(column, "column");
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
            return "HeadedTableColumn: {table: " + table + ", column: " + column + "}";
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

            if (!(o instanceof HeadedTableColumn)) {
                return false;
            }

            HeadedTableColumn other = (HeadedTableColumn) o;

            return other.table.equals(table) && other.column.equals(column);
        }
    }

    /**
     * Represents a concrete row within a specific table instance. Given this and a column
     * definition that corresponds to this row's table type (T), you will be able to look up the
     * contents of a specific cell.
     *
     * @param <T> The type of table this row is within.
     */
    final class Row<T extends Table<T>> implements Comparable<Row<T>> {
        private final T table;
        private final int index;

        public Row(T table, int index) {
            this.table = Objects.requireNonNull(table, "table");
            this.index = Objects.requireNonNull(index, "index");
        }

        public <U> U getCell(Column<T, U> column) {
            return column.getCell(getTable(), getIndex());
        }

        public T getTable() {
            return table;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "Row: {table: " + table + ", index: " + index + "}";
        }

        @Override
        public int compareTo(Row<T> o) {
            Objects.requireNonNull(o, "o");

            return index - o.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(table, index);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof Row)) {
                return false;
            }

            Row other = (Row) o;

            return other.index == index && other.table.equals(table);
        }
    }
}
