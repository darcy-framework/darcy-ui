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

import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.ElementHandler;
import com.redhat.darcy.ui.internal.ElementListHandler;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Proxy;
import java.util.List;

@RunWith(JUnit4.class)
public class ElementsTest {
    @Test
    public void shouldCreateProxyImplementingLazyElementUsingElementHandlerForElements() {
        Element element = Elements.element(By.id("test"));

        assertThat(element, instanceOf(Proxy.class));
        assertThat(element, instanceOf(HasElementContext.class));
        assertThat(Proxy.getInvocationHandler(element), instanceOf(ElementHandler.class));
    }

    @Test
    public void shouldCreateProxyImplementingLazyElementUsingElementListHandlerForElementLists() {
        List<Element> elementList = Elements.elements(By.id("test"));

        assertThat(elementList, instanceOf(Proxy.class));
        assertThat(elementList, instanceOf(HasElementContext.class));
        assertThat(Proxy.getInvocationHandler(elementList), instanceOf(ElementListHandler.class));
    }

    @Test
    public void shouldCreateProxyImplementingWrapsElementForElements() {
        Element element = Elements.element(By.id("test"));

        assertThat(element, instanceOf(WrapsElement.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfElementTypeIsNotAnInterfaceForElement() {
        Elements.element(AlwaysDisplayedLabel.class, By.id("test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfElementTypeIsNotAnInterfaceForElements() {
        Elements.elements(AlwaysDisplayedLabel.class, By.id("test"));
    }
}
