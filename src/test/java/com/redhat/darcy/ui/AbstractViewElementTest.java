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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.ui.internal.FindsByNested;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Proxy;

@RunWith(JUnit4.class)
public class AbstractViewElementTest {
    @Test
    public void shouldAllowSubclassesToReferenceParentElement() {
        AbstractViewElement viewElement = new AbstractViewElement(By.id("parent")) {};
        assertNotNull(viewElement.parent);
    }

    @Test
    public void shouldReturnByNestedUnderParentElementForByInner() {
        TestElementContext mockContext = mock(TestElementContext.class);
        Element mockElement = mock(Element.class);

        AbstractViewElement viewElement = new AbstractViewElement(mockElement) {};
        Locator inner = viewElement.byInner(By.id("child"));
        inner.find(TextInput.class, mockContext);

        verify(mockContext).findByNested(TextInput.class, mockElement, By.id("child"));
    }

    @Test
    public void shouldAssignParentElementToElementProxyIfConstructedWithLocator() {
        TestElementContext mockContext = mock(TestElementContext.class);

        AbstractViewElement viewElement = new AbstractViewElement(By.id("test")) {};

        assertThat(viewElement.parent, instanceOf(Proxy.class));

        ((HasElementContext) viewElement.parent).setContext(mockContext);
        ((WrapsElement) viewElement.parent).getWrappedElement(); // Causes the element reference to
                                                                 // be looked up.

        verify(mockContext).findById(anyObject(), eq("test"));
    }

    interface TestElementContext extends ElementContext, FindsByNested, FindsById {}
}
