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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
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
        Element customElement = selection.elementOfType(new FakeCustomElement(), mockLocator);

        assertThat(customElement, instanceOf(View.class));
        assertNotNull(((View) customElement).getContext());
    }

    @Test
    public void shouldReturnLazyListForCustomElementListsBackedByElementListFoundByLocator() {
        ElementContext mockContext = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);
        List<Element> backingList = new ArrayList<>();
        // Set up some state about the backing list (size is 2)
        backingList.add(new AlwaysDisplayedLabel());
        backingList.add(new AlwaysDisplayedLabel());
        when(mockLocator.findAll(Element.class, mockContext)).thenReturn(backingList);

        DefaultElementSelection selection = new DefaultElementSelection(mockContext);
        List<FakeCustomElement> elements = selection.elementsOfType(FakeCustomElement::new, mockLocator);

        assertThat(elements, instanceOf(LazyList.class));
        assertThat("Custom element list should be backed by list of elements found by " +
                        "locator.",
                elements.size(), is(equalTo(2)));
    }

    // TODO: Need to test that custom element is setup with correct context / locator
}
