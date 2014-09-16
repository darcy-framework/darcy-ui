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

import java.util.Iterator;

/**
 * A role interface for tables which show one of many possible pages at a time.
 */
public interface PaginatedTable<T extends PaginatedTable<T>> extends Table<T> {
    /**
     * @param page The specific page to navigate to, in a range from 1 to {@link #getMaxPages()},
     * inclusive.
     * @return this
     * @throws java.lang.IndexOutOfBoundsException if the page attempting to be navigated to is
     * greater than {@link #getMaxPages()}.
     */
    T toPage(int page);

    /**
     * @throws java.lang.IndexOutOfBoundsException if {@link #hasPreviousPage()} is false.
     */
    T previousPage();

    /**
     * @throws java.lang.IndexOutOfBoundsException if {@link #hasNextPage()} is false.
     */
    T nextPage();

    boolean hasNextPage();

    boolean hasPreviousPage();

    int getCurrentPage();

    /**
     * Note that not all table implementations will know all of their data at once, and so
     * calculating the total number of entries may take a considerable amount of time, and may even
     * require paging through every possible page. Should implementations require this, they must
     * return to the page that was open before calling this method.
     */
    int getTotalEntries();

    /**
     * @return An iterable which will cause the table to navigate to the first page, and every page
     * thereafter. The iterator returns the table itself, navigated to the next page.
     */
    default Iterable<T> ascendingPages() {
        return () -> new Iterator<T>() {
            int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor == 0 || hasNextPage();

            }

            @Override
            public T next() {
                return (cursor++ == 0) ? toPage(1) : nextPage();
            }
        };
    }

    /**
     * @return An iterable which will cause the table to navigate to the last page, and every page
     * prior. The iterator returns the table itself, navigated to the previous page. Note that not
     * all tables will be able to navigate to the last page directly, and some may require first
     * paging through all of the pages ascending until the last page can be determined.
     */
    default Iterable<T> descendingPages() {
        return () -> new Iterator<T>() {
            int cursor = getMaxPages() + 1;

            @Override
            public boolean hasNext() {
                return cursor == getMaxPages() + 1 || hasPreviousPage();

            }

            @Override
            public T next() {
                return (cursor-- == getMaxPages() + 1) ? toPage(getMaxPages()) : previousPage();
            }
        };
    }

    /**
     * Note that not all table implementations will know all of their data at once, and so
     * calculating the total number of entries may take a considerable amount of time, and may even
     * require paging through every possible page. Should implementations require this, they must
     * return to the page that was open before calling this method.
     *
     * <p>Since page numbers start from 1, this is equivalent to the number of the last page.
     */
    default int getMaxPages() {
        if (!hasNextPage()) {
            return getCurrentPage();
        }

        // Since getTotalEntries may cause the table to paginate to the last page, perform the
        // inverse to avoid calculating the total entries before determining the row count.
        return (int) Math.ceil((double) getTotalEntries() * (1.0 / (double) getRowCount()));
    }
}
