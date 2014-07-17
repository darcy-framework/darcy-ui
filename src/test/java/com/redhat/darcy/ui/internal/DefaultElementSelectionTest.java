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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.FakeCustomElement;
import com.redhat.darcy.ui.testing.doubles.NullContext;
import com.redhat.darcy.util.LazyList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class DefaultElementSelectionTest {
    @Test
    public void shouldLookupElementTypeUsingLocatorAndContext() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        selection.elementOfType(TextInput.class, mockLocator);

        verify(mockLocator).find(TextInput.class, mockContext);
    }

    @Test
    public void shouldLookupElementListUsingLocatorAndContext() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        selection.elementsOfType(TextInput.class, mockLocator);

        verify(mockLocator).findAll(TextInput.class, mockContext);
    }

    @Test
    public void shouldSetContextWithRootLocatorForCustomElements() {
        ContextThatFindsByChained mockContext = mock(ContextThatFindsByChained.class);
        ElementContext nullContext = new NullContext();
        Locator mockLocator = mock(Locator.class);

        when(mockContext.withRootLocator(anyObject())).thenReturn(nullContext);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        FakeCustomElement customElement = selection.elementOfType(new FakeCustomElement(), mockLocator);

        verify(mockContext).withRootLocator(mockLocator);
        assertSame(nullContext, customElement.getContext());
    }

    @Test
    public void shouldReturnLazyListForCustomElementLists() {
        // Given...
        ContextThatFindsByNested mockContext = mock(ContextThatFindsByNested.class);
        Locator mockLocator = mock(Locator.class);

        List<Element> backingList = new ArrayList<>();
        backingList.add(new AlwaysDisplayedLabel());
        backingList.add(new AlwaysDisplayedLabel());
        when(mockLocator.findAll(Element.class, mockContext)).thenReturn(backingList);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);

        // When...
        List<FakeCustomElement> elements = selection.elementsOfType(FakeCustomElement::new, mockLocator);

        // Then...
        assertThat(elements, instanceOf(LazyList.class));
    }

    @Test
    public void shouldBackCustomElementListWithEachElementFoundViaLocator() {
        // Given...
        ContextThatFindsByNested mockContext = mock(ContextThatFindsByNested.class);
        Locator mockLocator = mock(Locator.class);

        List<Element> backingList = new ArrayList<>();
        Label label1 = new AlwaysDisplayedLabel();
        Label label2 = new AlwaysDisplayedLabel();
        backingList.add(label1);
        backingList.add(label2);
        when(mockLocator.findAll(Element.class, mockContext)).thenReturn(backingList);

        ElementContext label1Context = new NullContext();
        ElementContext label2Context = new NullContext();
        when(mockContext.withRootElement(label1)).thenReturn(label1Context);
        when(mockContext.withRootElement(label2)).thenReturn(label2Context);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);

        // When...
        List<FakeCustomElement> elements = selection.elementsOfType(FakeCustomElement::new, mockLocator);

        // Then...
        assertThat(elements, instanceOf(LazyList.class));

        List<ElementContext> contexts = elements.stream()
                .map(View::getContext)
                .collect(Collectors.toList());

        assertThat(contexts, containsInAnyOrder(label1Context, label2Context));
    }

    /**
     * Contexts must find by chained in order to lookup a single custom element.
     */
    interface ContextThatFindsByChained extends ElementContext, FindsByChained {}

    /**
     * Contexts must find by nested in order to lookup many custom elements (to avoid all using the
     * same root element).
     */
    interface ContextThatFindsByNested extends ElementContext, FindsByNested {}
}
