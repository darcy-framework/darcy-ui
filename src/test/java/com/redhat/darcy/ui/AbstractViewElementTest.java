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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Button;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.internal.FindsByChained;
import com.redhat.darcy.ui.internal.FindsByNested;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AbstractViewElementTest {
    @Test
    public void shouldConstructChainedInnerLocatorWhenConstructedWithLocator() {
        TestElementContext mockContext = mock(TestElementContext.class);

        AbstractViewElement viewElement = new AbstractViewElement(By.id("parent")) {};
        Locator inner = viewElement.byInner(By.id("child"));
        inner.find(Button.class, mockContext);

        verify(mockContext).findByChained(Button.class, By.id("parent"), By.id("child"));
    }

    @Test
    public void shouldConstructNestedInnerLocatorWhenConstructedWithElement() {
        TestElementContext mockContext = mock(TestElementContext.class);
        Element mockElement = mock(Element.class);

        AbstractViewElement viewElement = new AbstractViewElement(mockElement) {};
        Locator inner = viewElement.byInner(By.id("child"));
        inner.find(TextInput.class, mockContext);

        verify(mockContext).findByNested(TextInput.class, mockElement, By.id("child"));
    }

    interface TestElementContext extends ElementContext, FindsByChained, FindsByNested {}
}
