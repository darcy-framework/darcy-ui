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

package com.redhat.darcy.ui.internal;

import com.redhat.darcy.ui.NullContextException;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.util.LazyList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class ViewList<T extends View> implements List<T>, HasElementContext {
    private final NestedViewFactory<? extends T> element;
    private final Locator locator;
    private LazyList<T> backingList;

    public ViewList(NestedViewFactory<? extends T> element, Locator locator) {
        this.element = element;
        this.locator = locator;
    }

    @Override
    public void setContext(ElementContext elementContext) {
        backingList = new LazyList<>(() -> locator.findAll(Element.class, elementContext)
                .stream()
                .map(e -> (T) element.newElement(e))
                .peek(v -> v.setContext(elementContext))
                .collect(Collectors.toList()));
    }

    @Override
    public boolean add(T t) {
        return backingList().add(t);
    }

    @Override
    public boolean remove(Object o) {
        return backingList().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return backingList().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return backingList().addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return backingList().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return backingList().retainAll(c);
    }

    @Override
    public void clear() {
        backingList().clear();
    }

    @Override
    public boolean equals(Object o) {
        return backingList().equals(o);
    }

    @Override
    public int hashCode() {
        return backingList().hashCode();
    }

    @Override
    public T get(int index) {
        return backingList().get(index);
    }

    @Override
    public T set(int index, T element) {
        return backingList().set(index, element);
    }

    @Override
    public void add(int index, T element) {
        backingList().add(index, element);
    }

    @Override
    public T remove(int index) {
        return backingList().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return backingList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backingList().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return backingList().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return backingList().listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return backingList().subList(fromIndex, toIndex);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return backingList().toArray(a);
    }

    @Override
    public Object[] toArray() {
        return backingList().toArray();
    }

    @Override
    public Iterator<T> iterator() {
        return backingList().iterator();
    }

    @Override
    public boolean contains(Object o) {
        return backingList().contains(o);
    }

    @Override
    public boolean isEmpty() {
        return backingList().isEmpty();
    }

    @Override
    public int size() {
        return backingList().size();
    }

    private List<T> backingList() {
        if (backingList == null) {
            throw new NullContextException();
        }

        return backingList;
    }
}
