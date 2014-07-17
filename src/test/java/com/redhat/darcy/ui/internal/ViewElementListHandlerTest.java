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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.FakeCustomElement;
import com.redhat.darcy.ui.testing.doubles.NullContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RunWith(JUnit4.class)
public class ViewElementListHandlerTest {
    Method setContext;
    ContextThatCanFindByNested mockContext;
    ViewElementListHandler handler;
    Locator mockLocator;

    @Before
    public void setup() throws NoSuchMethodException {
        setContext = HasElementContext.class.getMethod("setContext", ElementContext.class);

        mockContext = mock(ContextThatCanFindByNested.class);
        mockLocator = mock(Locator.class);

        handler = new ViewElementListHandler(FakeCustomElement::new, mockLocator);
    }

    @Test
    public void shouldImplementSetContextAndUseContextToLookupElement() throws Throwable {
        // This should set the mock context to be used later
        handler.invoke(null, setContext, new Object[] { mockContext });

        // This should cause the context to be used to find the element if it was properly set
        handler.invoke(null, List.class.getMethod("size"), new Object[] {});

        // Verify the context was used
        verify(mockLocator).findAll(anyObject(), eq(mockContext));
    }

    @Test
    public void shouldCreateViewsForEachFoundElement() throws Throwable {
        List<Object> backingList = new ArrayList<>(2);
        backingList.add(mock(Element.class));
        backingList.add(mock(Element.class));
        when(mockLocator.findAll(anyObject(), anyObject())).thenReturn(backingList);

        // Necessary to avoid NullContextException
        handler.invoke(null, setContext, new Object[] { mockContext });

        int size = (int) handler.invoke(null, List.class.getMethod("size"), new Object[] {});

        assertThat(size, equalTo(backingList.size()));
    }

    @Test
    public void shouldCreateViewsUsingSupplier() throws Throwable {
        // In order to get one, there needs to be a backing element
        List<Object> backingList = new ArrayList<>(1);
        backingList.add(mock(Element.class));
        when(mockLocator.findAll(anyObject(), anyObject())).thenReturn(backingList);

        handler = new ViewElementListHandler(FakeCustomElement::new, mockLocator);

        // Necessary to avoid NullContextException
        handler.invoke(null, setContext, new Object[] { mockContext });

        Object result = handler.invoke(null,
                List.class.getMethod("get", int.class), new Object[] {0});

        assertThat(result, instanceOf(FakeCustomElement.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAssignNestedContextToEachViewForEachElement() throws Throwable {
        List<Object> backingList = new ArrayList<>(2);
        Label label1 = new AlwaysDisplayedLabel();
        Label label2 = new AlwaysDisplayedLabel();
        backingList.add(label1);
        backingList.add(label2);
        when(mockLocator.findAll(anyObject(), anyObject())).thenReturn(backingList);

        ElementContext label1Context = new NullContext();
        ElementContext label2Context = new NullContext();
        when(mockContext.withRootElement(label1)).thenReturn(label1Context);
        when(mockContext.withRootElement(label2)).thenReturn(label2Context);

        // Necessary to avoid NullContextException
        handler.invoke(null, setContext, new Object[] { mockContext });

        Iterator<Object> iter = (Iterator<Object>) handler.invoke(null,
                List.class.getMethod("iterator"), new Object[]{});

        List<ElementContext> contexts = StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iter, 0), false)
                .map(o -> (FakeCustomElement) o)
                .map(View::getContext)
                .collect(Collectors.toList());

        assertThat(contexts, containsInAnyOrder(label1Context, label2Context));
    }

    /**
     * In order to find a list of custom elements, the context must support nested location
     * strategy.
     * @see com.redhat.darcy.ui.By#nested
     */
    interface ContextThatCanFindByNested extends ElementContext, FindsByNested {}
}
