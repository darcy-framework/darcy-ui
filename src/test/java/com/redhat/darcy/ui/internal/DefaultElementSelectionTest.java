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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.ViewElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.FakeCustomElement;

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
    public void shouldSetContextOnViewElements() {
        ElementContext mockContext = mock(ElementContext.class);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        FakeCustomElement customElement = selection.viewOfType(FakeCustomElement::new,
                mock(Locator.class));

        assertSame(mockContext, customElement.getContext());
    }

    @Test
    public void shouldPassLocatorToViewElementConstructor() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);
        ChainedViewFactory mockConstructor = mock(ChainedViewFactory.class);
        when(mockConstructor.newElement(any(Locator.class))).thenReturn(mock(ViewElement.class));

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        selection.viewOfType(mockConstructor, mockLocator);

        verify(mockConstructor).newElement(mockLocator);
    }

    @Test
    public void shouldReturnLazyListForViewElementLists() {
        // Given...
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);

        List<Element> backingList = new ArrayList<>();
        backingList.add(new AlwaysDisplayedLabel());
        backingList.add(new AlwaysDisplayedLabel());
        when(mockLocator.findAll(Element.class, mockContext)).thenReturn(backingList);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);

        // When...
        List<FakeCustomElement> elements = selection.viewsOfType(FakeCustomElement::new, mockLocator);

        // Should not yet have actually found the elements
        verify(mockLocator, never()).findAll(any(Class.class), any(Context.class));

        // Until we try to do something with the list..
        elements.isEmpty();

        verify(mockLocator).findAll(Element.class, mockContext);
    }

    @Test
    public void shouldBackViewElementListWithEachElementFoundViaLocator() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);

        List<Element> backingList = new ArrayList<>(2);
        Element element1 = new AlwaysDisplayedLabel();
        Element element2 = new AlwaysDisplayedLabel();
        backingList.add(element1);
        backingList.add(element2);

        when(mockLocator.findAll(Element.class, mockContext)).thenReturn(backingList);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        List<FakeCustomElement> elements = selection.viewsOfType(FakeCustomElement::new, mockLocator);

        List<Element> parentElements = elements
                .stream()
                .map(FakeCustomElement::getParentElement)
                .collect(Collectors.toList());

        assertThat(parentElements, containsInAnyOrder(element1, element2));
    }
}
