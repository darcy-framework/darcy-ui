/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy.

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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.NullContextException;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.internal.ElementHandler;
import com.redhat.darcy.ui.testing.doubles.DummyContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

@RunWith(JUnit4.class)
public class ElementHandlerTest {
    Method setContext;
    DummyContext mockContext;
    ElementHandler handler;
    Element mockElement;
    Locator mockLocator;

    @Before
    public void setup() throws NoSuchMethodException {
        setContext = HasElementContext.class.getMethod("setContext", ElementContext.class);

        mockContext = Mockito.mock(DummyContext.class);
        mockElement = Mockito.mock(Element.class);
        mockLocator = Mockito.mock(Locator.class);

        Mockito.when(mockLocator.find(Matchers.anyObject(), Matchers.anyObject())).thenReturn(mockElement);

        handler = new ElementHandler(TextInput.class, mockLocator);
    }

    @Test
    public void shouldImplementSetContextAndUseContextToLookupElement() throws Throwable {
        // This should set the mock context to be used later
        handler.invoke(null, setContext, new Object[] { mockContext });

        // This should cause the context to be used to find the element if it was properly set
        handler.invoke(null, Element.class.getMethod("isDisplayed"), new Object[] {});

        // Verify the context was used
        Mockito.verify(mockLocator).find(Matchers.anyObject(), Matchers.eq(mockContext));
    }

    @Test
    public void shouldLookupElementWithAppropriateType() throws Throwable {
        // This should set the mock context to be used later
        handler.invoke(null, setContext, new Object[] { mockContext });

        // This should cause the context to be used to find the element if it was properly set
        handler.invoke(null, Element.class.getMethod("isDisplayed"), new Object[] {});

        // Verify the mock was used
        Mockito.verify(mockLocator).find(Matchers.eq(TextInput.class), Matchers.anyObject());
    }

    @Test
    public void shouldForwardMethodsToFoundElement() throws Throwable {
        // This should set the mock context to be used later
        handler.invoke(null, setContext, new Object[] { mockContext });

        // This should cause the context to be used to find the element if it was properly set
        handler.invoke(null, Findable.class.getMethod("isPresent"), new Object[] {});

        Mockito.verify(mockElement).isPresent();
    }

    @Test
    public void shouldCacheAndReuseFoundElement() throws Throwable {
        // This should set the mock context to be used later
        handler.invoke(null, setContext, new Object[] { mockContext });

        // This should cause the context to be used to find the element if it was properly set
        handler.invoke(null, Element.class.getMethod("isDisplayed"), new Object[] {});
        handler.invoke(null, Element.class.getMethod("isDisplayed"), new Object[] {});

        Mockito.verify(mockLocator, Mockito.times(1)).find(Matchers.anyObject(), Matchers.anyObject());
    }

    @Test(expected = TestException.class)
    public void shouldThrowCauseOfInvocationTargetExceptions() throws Throwable {
        Mockito.when(mockElement.isDisplayed()).thenThrow(new TestException());

        // This should set the mock context to be used later
        handler.invoke(null, setContext, new Object[]{mockContext});

        // This should cause the context to be used to find the element if it was properly set
        handler.invoke(null, Element.class.getMethod("isDisplayed"), new Object[]{});
    }

    @Test(expected = NullContextException.class)
    public void shouldThrowNullContextExceptionIfMethodIsCalledWithoutAContextBeingSet() throws
            Throwable {
        handler.invoke(null, Element.class.getMethod("isDisplayed"), new Object[] {});
    }

    private class TestException extends RuntimeException {}
}
