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

package com.redhat.darcy.ui.elements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.ViewElementHandler;
import com.redhat.darcy.ui.ViewElementListHandler;
import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.ElementHandler;
import com.redhat.darcy.ui.ElementListHandler;
import com.redhat.darcy.ui.HasElementContext;
import com.redhat.darcy.ui.LazyElement;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.FakeCustomElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class ElementsTest {
    @Test
    public void shouldCreateProxyImplementingLazyElementUsingElementHandlerForElements() {
        Element element = Elements.element(By.id("test"));

        assertThat(element, instanceOf(Proxy.class));
        assertThat(element, instanceOf(LazyElement.class));
        assertThat(Proxy.getInvocationHandler(element), instanceOf(ElementHandler.class));
    }

    @Test
    public void shouldCreateProxyImplementingLazyElementUsingElementListHandlerForElementLists() {
        List<Element> elementList = Elements.elements(By.id("test"));

        assertThat(elementList, instanceOf(Proxy.class));
        assertThat(elementList, instanceOf(LazyElement.class));
        assertThat(Proxy.getInvocationHandler(elementList), instanceOf(ElementListHandler.class));
    }

    @Test
    public void shouldCreateProxyImplementingLazyElementUsingCustomElementHandlerForCustomElements() {
        Element element = Elements.element(Element.class, By.id("id"), new FakeCustomElement());

        assertThat(element, instanceOf(Proxy.class));
        assertThat(element, instanceOf(LazyElement.class));
        assertThat(Proxy.getInvocationHandler(element), instanceOf(ViewElementHandler.class));
    }

    @Test
    public void shouldCreateProxyImplementingLazyElementUsingCustomElementListHandlerForCustomElementLists() {
        List<Element> elementList = Elements.elements(Element.class, By.id("test"),
                FakeCustomElement::new);

        assertThat(elementList, instanceOf(Proxy.class));
        assertThat(elementList, instanceOf(LazyElement.class));
        assertThat(Proxy.getInvocationHandler(elementList),
                instanceOf(ViewElementListHandler.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfElementTypeIsNotAnInterfaceForElement() {
        Elements.element(AlwaysDisplayedLabel.class, By.id("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfElementTypeIsNotAnInterfaceForElements() {
        Elements.elements(AlwaysDisplayedLabel.class, By.id("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfElementTypeIsNotAnInterfaceForCustomElement() {
        Elements.element(FakeCustomElement.class, By.id("test"), new FakeCustomElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfViewImplementationIsNotAViewForCustomElement() {
        Elements.element(Element.class, By.id("test"), new AlwaysDisplayedLabel());
    }

    @Test
    public void shouldNotThrowExceptionIfElementTypeIsNotAnInterfaceForCustomElementLists() {
        Elements.elements(FakeCustomElement.class, By.id("test"), FakeCustomElement::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfImplementationDoesNotImplementViewForCustomElementLists() {
        List<Element> backingList = new ArrayList<>();
        backingList.add(mock(Element.class));
        Locator mockLocator = mock(Locator.class);
        when(mockLocator.findAll(eq(Element.class), anyObject())).thenReturn(backingList);

        // Unfortunately can't reflect type information (especially on method refs or lambdas)
        // about generics
        List<Label> labels = Elements.elements(Label.class, mockLocator, AlwaysDisplayedLabel::new);

        // Needs context to lookup elements (otherwise NullContextException)
        ((HasElementContext) labels).setContext(mock(ElementContext.class));

        // Will call supplier for views, checking for type compatibility
        labels.get(0);
    }
}
