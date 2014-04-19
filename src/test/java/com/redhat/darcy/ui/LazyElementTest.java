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

package com.redhat.darcy.ui;

import static org.junit.Assert.assertEquals;

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.Elements;
import com.redhat.darcy.ui.elements.Label;
import com.redhat.darcy.ui.elements.LazyElement;

import org.junit.Test;

public class LazyElementTest {
    @Test(expected = NullViewException.class)
    public void shouldThrowNullViewExceptionIfNotInitialized() {
        Element element = Elements.element(By.id("test"));
        
        element.isDisplayed();
    }
    
    @Test(expected = NullContextException.class)
    public void shouldThrowNullContextExceptionIfAssociatedViewHasNoContext() {
        Element element = Elements.element(By.id("test"));
        
        ((LazyElement) element).setView(new DummyView());
        
        element.isDisplayed();
    }
    
    @Test
    public void shouldAssociateWithAViewContext() {
        Label label = Elements.label(By.id("test"));
        
        ((LazyElement) label).setView(new DummyView().setContext(new DummyContext()));
        
        // DummyContext always returns the same label implementation for every element
        assertEquals("Expected lazy element to be forwarding methods to implementation defined by "
                + "context.", new AlwaysDisplayedLabel().readText(), label.readText());
    }
}
