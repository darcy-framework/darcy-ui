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
import com.redhat.darcy.ui.api.elements.PaginatedTable;

public class FakePaginatedTable implements PaginatedTable<FakePaginatedTable> {
    public static class Builder {
        private int rowsPerPage = 10;
        private int totalEntries = 10;

        public Builder rowsPerPage(int rowsPerPage) {
            this.rowsPerPage = rowsPerPage;

            return this;
        }

        public Builder totalEntries(int totalEntries) {
            this.totalEntries = totalEntries;

            return this;
        }

        public FakePaginatedTable build() {
            return new FakePaginatedTable(rowsPerPage, totalEntries);
        }
    }

    private final int rowsPerPage;
    private final int totalEntries;

    private int curPage = 1;

    protected FakePaginatedTable(int rowsPerPage, int totalEntries) {
        this.rowsPerPage = rowsPerPage;
        this.totalEntries = totalEntries;
    }

    @Override
    public FakePaginatedTable toPage(int page) {
        curPage = page;

        return this;
    }

    @Override
    public FakePaginatedTable previousPage() {
        if (!hasPreviousPage()) {
            throw new IndexOutOfBoundsException();
        }

        curPage--;

        return this;
    }

    @Override
    public FakePaginatedTable nextPage() {
        if (!hasNextPage()) {
            throw new IndexOutOfBoundsException();
        }

        curPage++;

        return this;
    }

    @Override
    public boolean hasNextPage() {
        return getCurrentPage() * rowsPerPage < totalEntries;

    }

    @Override
    public boolean hasPreviousPage() {
        return getCurrentPage() > 1;
    }

    @Override
    public int getCurrentPage() {
        return curPage;
    }

    @Override
    public int getTotalEntries() {
        return totalEntries;
    }

    @Override
    public int getRowCount() {
        // If we're on last page...
        if (getCurrentPage() * rowsPerPage > getTotalEntries()) {
            return getTotalEntries() - (getCurrentPage() - 1) * rowsPerPage;
        }

        return rowsPerPage;
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
