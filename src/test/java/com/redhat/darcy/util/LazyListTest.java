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

package com.redhat.darcy.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.function.Supplier;

@RunWith(JUnit4.class)
public class LazyListTest {
    interface ObjectListSupplier extends Supplier<List<Object>> {}
    interface ObjectList extends List<Object> {}

    Supplier<List<Object>> mockListSupplier;
    List<Object> mockList;
    LazyList<Object> lazyList;

    @Before
    public void setup() {
        mockListSupplier = mock(ObjectListSupplier.class);
        mockList = mock(ObjectList.class);
        lazyList = new LazyList<>(mockListSupplier);

        when(mockListSupplier.get()).thenReturn(mockList);
    }

    @Test
    public void shouldCacheListViaSize() {
        lazyList.size();
        lazyList.size();

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaIsEmpty() {
        lazyList.isEmpty();
        lazyList.isEmpty();

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaContains() {
        lazyList.contains(null);
        lazyList.contains(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaIterator() {
        lazyList.iterator();
        lazyList.iterator();

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaToArray() {
        lazyList.toArray();
        lazyList.toArray();

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaToArrayWithType() {
        lazyList.toArray(null);
        lazyList.toArray(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaAdd() {
        lazyList.add(null);
        lazyList.add(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaRemove() {
        lazyList.remove(null);
        lazyList.remove(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaContainsAll() {
        lazyList.containsAll(null);
        lazyList.containsAll(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaAddAll() {
        lazyList.addAll(null);
        lazyList.addAll(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaAddAllWithIndex() {
        lazyList.addAll(1, null);
        lazyList.addAll(1, null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaRemoveAll() {
        lazyList.removeAll(null);
        lazyList.removeAll(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaRetainAll() {
        lazyList.retainAll(null);
        lazyList.retainAll(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaClear() {
        lazyList.clear();
        lazyList.clear();

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaGet() {
        lazyList.get(1);
        lazyList.get(1);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaSet() {
        lazyList.set(1, null);
        lazyList.set(1, null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaAddWithIndex() {
        lazyList.add(1, null);
        lazyList.add(1, null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaRemoveWithIndex() {
        lazyList.remove(1);
        lazyList.remove(1);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaIndexOf() {
        lazyList.indexOf(null);
        lazyList.indexOf(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaLastIndexOf() {
        lazyList.lastIndexOf(null);
        lazyList.lastIndexOf(null);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaListIterator() {
        lazyList.listIterator();
        lazyList.listIterator();

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaListIteratorWithIndex() {
        lazyList.listIterator(1);
        lazyList.listIterator(1);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldCacheListViaSubList() {
        lazyList.subList(1, 2);
        lazyList.subList(1, 2);

        verify(mockListSupplier, times(1)).get();
    }

    @Test
    public void shouldRecacheIfCacheIsInvalidated() {
        lazyList.size();
        lazyList.invalidateCache();
        lazyList.size();

        verify(mockListSupplier, times(2)).get();
    }
}
