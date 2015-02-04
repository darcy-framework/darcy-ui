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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RowTest {
    @Test
    public void shouldBeEquivalentToRowsForEquivalentTablesAtSameIndex() {
        Table.Row<TestTable> row = new Table.Row<>(new TestTable(By.id("table")), 10);

        assertThat(row, equalTo(new Table.Row<>(new TestTable(By.id("table")), 10)));
        assertThat(row, equalTo(row));
    }

    @Test
    public void shouldNotBeEquivalentToAnythingElse() {
        Table.Row<TestTable> row = new Table.Row<>(new TestTable(By.id("table")), 10);

        assertThat(row, not(equalTo(100)));
        assertThat(row, not(equalTo(new Table.Row<>(new TestTable(By.id("table")), 0))));
        assertThat(row, not(equalTo(new Table.Row<>(new TestTable(By.id("otherTable")), 10))));
    }

    @Test
    public void shouldHaveEquivalentHashCodeForEquivalentRows() {
        Table.Row<TestTable> row = new Table.Row<>(new TestTable(By.id("table")), 10);

        assertThat(row.hashCode(),
                equalTo(new Table.Row<>(new TestTable(By.id("table")), 10).hashCode()));

    }

    @Test
    public void shouldDescribeTableAndIndexInToString() {
        TestTable table = new TestTable(By.id("table"));
        Table.Row<TestTable> row = new Table.Row<>(table, 5);

        String tableToString = table.toString();

        String rowToString = row.toString();

        assertThat(rowToString, containsString("table: " + tableToString));
        assertThat(rowToString, containsString("index: " + 5));
    }

    @Test
    public void shouldBeFinal() {
        assertTrue("Row should not be extendable.", isFinal(Table.Row.class.getModifiers()));
    }

    private static class TestTable implements Table<TestTable> {
        private final Locator parent;

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
