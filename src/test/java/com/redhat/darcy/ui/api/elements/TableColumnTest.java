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

import static java.lang.reflect.Modifier.isFinal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Table.TableColumn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TableColumnTest {
    @Test
    public void shouldBeEquivalentToTableColumnsForEquivalentTablesAndEquivalentColumns() {
        TestTable testTable = new TestTable(By.id("test"));
        TableColumn column = new TableColumn(testTable, TestTable.TEST_COLUMN);
        TableColumn equivalentColumn =
                new TableColumn(new TestTable(By.id("test")), TestTable.TEST_COLUMN);

        assertThat(column, equalTo(equivalentColumn));
        assertThat(column, equalTo(column));
    }

    @Test
    public void shouldNotBeEquivalentToAnythingElse() {
        TestTable testTable = new TestTable(By.id("test"));
        TableColumn column = new TableColumn(testTable, TestTable.TEST_COLUMN);

        assertThat(column, not(equalTo(new TableColumn(
                new TestTable(By.id("not_test")), TestTable.TEST_COLUMN))));
        assertThat(column, not(equalTo(new TableColumn(
                new TestTable(By.id("test")), (t, i) -> null))));
        assertThat(column, not(equalTo(100)));
    }

    @Test
    public void shouldHaveEquivalentHashCodeForEquivalentTableColumns() {
        TestTable testTable = new TestTable(By.id("test"));
        TableColumn column = new TableColumn(testTable, TestTable.TEST_COLUMN);
        TableColumn equivalentColumn =
                new TableColumn(new TestTable(By.id("test")), TestTable.TEST_COLUMN);

        assertThat(column.hashCode(), equalTo(equivalentColumn.hashCode()));
    }

    @Test
    public void shouldDescribeTableAndIndexInToString() {
        TestTable table = new TestTable(By.id("table"));
        TableColumn column = new TableColumn<>(table, TestTable.TEST_COLUMN);

        String columnToString = column.toString();

        assertThat(columnToString, containsString("table: " + table.toString()));
        assertThat(columnToString, containsString("column: " + TestTable.TEST_COLUMN.toString()));
    }

    @Test
    public void shouldBeFinal() {
        assertTrue("TableColumn should not be extendable.",
                isFinal(TableColumn.class.getModifiers()));
    }

    private static class TestTable implements Table<TestTable> {
        private final Locator parent;

        static Column<TestTable, String> TEST_COLUMN = (table, rowIndex) -> null;

        private TestTable(Locator parent) {
            this.parent = parent;
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }

        @Override
        public int getRowCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public void setContext(ElementContext context) {

        }

        @Override
        public ElementContext getContext() {
            return null;
        }

        @Override
        public boolean isLoaded() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TestTable testTable = (TestTable) o;

            if (parent != null ? !parent.equals(testTable.parent) : testTable.parent != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return parent != null ? parent.hashCode() : 0;
        }
    }
}
