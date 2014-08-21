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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.AbstractView;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class ViewListTest {
    public class MyNestedView extends AbstractView {
        public MyNestedView(Element parent) {}
    }

    @Test
    public void shouldSetContextOnViewsWithinList() {
        ElementContext context = mock(ElementContext.class);
        Locator mockLocator = mock(Locator.class);

        List<Element> parentElements = new ArrayList<>();
        parentElements.add(new AlwaysDisplayedLabel());
        parentElements.add(new AlwaysDisplayedLabel());

        when(mockLocator.findAll(Element.class, context)).thenReturn(parentElements);

        ViewList<MyNestedView> viewList = new ViewList<>(MyNestedView::new, mockLocator);
        viewList.setContext(context);

        assertEquals(2, viewList.size());
        assertSame(context, viewList.get(0).getContext());
        assertSame(context, viewList.get(1).getContext());
    }
}
