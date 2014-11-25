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
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.ui.testing.doubles.StubTable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class TableTest {
    @Test
    public void shouldIterateOverVisibleRows() {
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 99;
            }
        };

        int curRow = 0;

        for (Table.Row row : testTable.rows()) {
            curRow++;
            assertThat("Unexpected row index in iterator.", row.getIndex(), equalTo(curRow));
            assertSame(testTable, row.getTable());
        }

        assertThat("Row iterator did not iterator over all visible rows.", curRow, equalTo(99));
    }

    @Test
    public void shouldGetRows() {
        StubTable testTable = new StubTable();
        Table.Row row = testTable.getRow(2);

        assertThat("Unexpected row index.", row.getIndex(), equalTo(2));
        assertSame(testTable, row.getTable());
    }

    @Test
    public void shouldGetColumns() {
        Table.Column<StubTable, Integer> testColumn = mock(TestColumn.class);

        StubTable testTable = new StubTable();
        Table.TableColumn column = testTable.getColumn(testColumn);

        assertSame(testTable, column.getTable());

        column.getCell(11);

        verify(testColumn).getCell(testTable, 11);
    }

    @Test
    public void shouldGetHeadedColumns() {
        Table.ColumnWithHeader<StubTable, Integer, String> testColumn =
                mock(TestColumnWithHeader.class);

        StubTable testTable = new StubTable();
        Table.HeadedTableColumn column = testTable.getColumn(testColumn);

        assertSame(testTable, column.getTable());

        column.getCell(11);
        column.getHeader();

        verify(testColumn).getCell(testTable, 11);
        verify(testColumn).getHeader(testTable);
    }

    @Test
    public void shouldGetHeaders() {
        Table.Header<StubTable, String> testColumn = t -> "the header";

        StubTable testTable = new StubTable();

        assertThat(testTable.getHeader(testColumn), equalTo("the header"));
    }

    @Test
    public void shouldGetCells() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;

        StubTable testTable = new StubTable();

        assertThat("Wrong row index used to retrieve cell contents.",
                testTable.getCell(testColumn, 5), equalTo(5));
    }

    @Test
    public void shouldGetRowsWhereWithPredicateInOrder() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 50;
            }
        };

        List<Table.Row<StubTable>> rows = testTable.getRowsWhere(testColumn, i -> i > 10)
                .collect(Collectors.toList());

        int curRow = 10;

        for (Table.Row<StubTable> row : rows) {
            curRow++;

            assertThat(row.getIndex(), equalTo(curRow));
            assertSame(testTable, row.getTable());
        }

        assertThat("Did not return all rows that satisfied predicate.", curRow, equalTo(50));
    }

    @Test
    public void shouldGetFirstRowWhereWithPredicateInOrder() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 50;
            }
        };

        Optional<Table.Row<StubTable>> row = testTable.getFirstRowWhere(testColumn, i -> i > 10);

        int curRow = 11;
        assertThat(row.get().getIndex(), equalTo(curRow));
        assertSame(testTable, row.get().getTable());
    }

    @Test
    public void shouldGetRowsWhereWithMatcherInOrder() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 50;
            }
        };

        List<Table.Row<StubTable>> rows = testTable.getRowsWhere(testColumn,
                greaterThan(10))
                .collect(Collectors.toList());

        int curRow = 10;

        for (Table.Row<StubTable> row : rows) {
            curRow++;

            assertThat(row.getIndex(), equalTo(curRow));
            assertSame(testTable, row.getTable());
        }

        assertThat("Did not return all rows that satisfied matcher.", curRow, equalTo(50));
    }

    @Test
    public void shouldGetFirstRowWhereWithMatcherInOrder() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 50;
            }
        };

        Optional<Table.Row<StubTable>> row = testTable.getFirstRowWhere(testColumn, greaterThan(10));

        int curRow = 11;
        assertThat(row.get().getIndex(), equalTo(curRow));
        assertSame(testTable, row.get().getTable());
    }

    @Test
    public void shouldGetCellsWhereWithPredicateInOrder() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 50;
            }
        };

        List<Integer> cells = testTable.getCellsWhere(testColumn, i -> i > 10)
                .collect(Collectors.toList());

        List<Integer> expected = new ArrayList<>(39);

        for (int i = 11; i <= 50; i++) {
            expected.add(i);
        }

        assertThat("Did not return all cells that satisfied predicate.", cells, equalTo(expected));
    }

    @Test
    public void shouldGetCellsWhereWithMatcherInOrder() {
        Table.Column<StubTable, Integer> testColumn = (t, r) -> r;
        StubTable testTable = new StubTable() {
            @Override
            public int getRowCount() {
                return 50;
            }
        };

        List<Integer> cells = testTable.getCellsWhere(testColumn, greaterThan(10))
                .collect(Collectors.toList());

        List<Integer> expected = new ArrayList<>(39);

        for (int i = 11; i <= 50; i++) {
            expected.add(i);
        }

        assertThat("Did not return all cells that satisfied matcher.", cells, equalTo(expected));
    }

    interface TestColumn extends Table.Column<StubTable, Integer> {}
    interface TestColumnWithHeader extends Table.ColumnWithHeader<StubTable, Integer, String> {}
}
