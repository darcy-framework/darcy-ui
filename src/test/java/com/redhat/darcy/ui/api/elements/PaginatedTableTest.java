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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.redhat.darcy.ui.testing.doubles.FakePaginatedTable;
import com.redhat.darcy.ui.testing.doubles.StubPaginatedTable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PaginatedTableTest {
    @Test
    public void shouldIterateThroughPagesAscending() {
        FakePaginatedTable testTable = new FakePaginatedTable.Builder()
                .rowsPerPage(10)
                .totalEntries(95)
                .build();

        int curPage = 0;

        for (FakePaginatedTable table : testTable.ascendingPages()) {
            curPage++;

            assertThat(table.getCurrentPage(), equalTo(curPage));
        }

        assertThat("Iterated through unexpected number of pages.", curPage, equalTo(10));
    }

    @Test
    public void shouldStartAtFirstPageWhenIteratingAscending() {
        FakePaginatedTable testTable = new FakePaginatedTable.Builder()
                .rowsPerPage(10)
                .totalEntries(95)
                .build();

        testTable.toPage(5);

        testTable.ascendingPages().iterator().next();

        assertThat(testTable.getCurrentPage(), equalTo(1));
    }
    @Test
    public void shouldIterateThroughPagesDescending() {
        FakePaginatedTable testTable = new FakePaginatedTable.Builder()
                .rowsPerPage(10)
                .totalEntries(95)
                .build();

        int curPage = 11;
        testTable.toPage(10);

        for (FakePaginatedTable table : testTable.descendingPages()) {
            curPage--;

            assertThat(table.getCurrentPage(), equalTo(curPage));
        }

        assertThat("Iteration through pages stopped early.", curPage, equalTo(1));
    }

    @Test
    public void shouldStartAtLastPageWhenIteratingDescending() {
        FakePaginatedTable testTable = new FakePaginatedTable.Builder()
                .rowsPerPage(10)
                .totalEntries(95)
                .build();

        testTable.descendingPages().iterator().next();

        assertThat(testTable.getCurrentPage(), equalTo(10));
    }

    @Test
    public void shouldCalculateMaxPagesFromRowCountAndTotalEntries() {
        FakePaginatedTable testTable = new FakePaginatedTable.Builder()
                .rowsPerPage(7)
                .totalEntries(95)
                .build();

        assertThat(testTable.getMaxPages(), equalTo(14));
    }

    @Test
    public void shouldUseCurrentPageAsLastPageIfTableDoesNotHaveNextPage() {
        StubPaginatedTable testTable = new StubPaginatedTable() {
            @Override
            public int getCurrentPage() {
                return 10;
            }

            @Override
            public boolean hasNextPage() {
                return false;
            }
        };

        assertThat(testTable.getMaxPages(), equalTo(10));
    }
}
