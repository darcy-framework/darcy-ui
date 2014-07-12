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

package com.redhat.darcy.ui;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.TextInput;
import com.redhat.darcy.ui.testing.doubles.FakeCustomElement;
import com.redhat.darcy.util.LazyList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

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
    public void shouldReturnViewWithSetContextForCustomElements() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        Element customElement = selection.elementOfType(Element.class, mockLocator,
                new FakeCustomElement());

        assertThat(customElement, instanceOf(View.class));
        assertNotNull(((View) customElement).getContext());
    }

    @Test
    public void shouldReturnLazyListForCustomElementLists() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);
        List<Element> listSpy = spy(new ArrayList<>());

        when(mockLocator.findAll(Element.class, mockContext)).thenReturn(listSpy);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        List<Element> elements = selection.elementsOfType(Element.class, mockLocator,
                FakeCustomElement::new);

        elements.size();

        assertThat(elements, instanceOf(LazyList.class));
        verify(listSpy).size();
    }

    interface ElementList extends List<Element> {}
}
