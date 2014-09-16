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

import static com.redhat.darcy.ui.api.elements.SortableTable.SortDirection;
import static com.redhat.darcy.ui.api.elements.SortableTable.SortableColumn;
import static com.redhat.darcy.ui.api.elements.SortableTable.SortableColumnWithHeader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.testing.doubles.StubSortableTable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SortableTableTest {
    @Test
    public void shouldSortAscending() {
        SortableColumn<StubSortableTable, String> mockColumn = mock(TestSortableColumn.class);
        StubSortableTable testTable = new StubSortableTable();

        testTable.sort(mockColumn, SortDirection.ASCENDING);

        verify(mockColumn).sort(testTable, SortDirection.ASCENDING);
    }

    @Test
    public void shouldSortDescending() {
        SortableColumn<StubSortableTable, String> mockColumn = mock(TestSortableColumn.class);
        StubSortableTable testTable = new StubSortableTable();

        testTable.sort(mockColumn, SortDirection.DESCENDING);

        verify(mockColumn).sort(testTable, SortDirection.DESCENDING);
    }

    @Test
    public void shouldGetSortDirection() {
        SortableColumn<StubSortableTable, String> mockColumn = mock(TestSortableColumn.class);
        StubSortableTable testTable = new StubSortableTable();

        when(mockColumn.getSortDirection(testTable)).thenReturn(SortDirection.DESCENDING);

        assertThat(testTable.getSortDirection(mockColumn), equalTo(SortDirection.DESCENDING));
    }

    @Test
    public void shouldGetSortableColumns() {
        SortableColumn<StubSortableTable, String> testColumn = mock(TestSortableColumn.class);
        StubSortableTable testTable = new StubSortableTable();

        SortableTable.SortableTableColumn column = testTable.getColumn(testColumn);

        assertSame(testTable, column.getTable());

        column.getCell(7);
        column.sort(SortDirection.ASCENDING);

        verify(testColumn).getCell(testTable, 7);
        verify(testColumn).sort(testTable, SortDirection.ASCENDING);
    }

    @Test
    public void shouldGetHeadedSortableColumns() {
        SortableColumnWithHeader<StubSortableTable, String, String> testColumn =
                mock(TestSortableColumnWithHeader.class);
        StubSortableTable testTable = new StubSortableTable();

        SortableTable.SortableHeadedTableColumn column = testTable.getColumn(testColumn);

        assertSame(testTable, column.getTable());

        column.getCell(7);
        column.getHeader();
        column.sort(SortDirection.DESCENDING);

        verify(testColumn).getCell(testTable, 7);
        verify(testColumn).getHeader(testTable);
        verify(testColumn).sort(testTable, SortDirection.DESCENDING);
    }

    interface TestSortableColumn extends SortableColumn<StubSortableTable, String> {}
    interface TestSortableColumnWithHeader extends
            SortableColumnWithHeader<StubSortableTable, String, String> {}
}
