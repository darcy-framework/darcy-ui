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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.annotations.NotRequired;
import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.annotations.RequireAll;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.Analyzer;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.AlwaysMetCondition;
import com.redhat.darcy.ui.testing.doubles.NeverDisplayedElement;
import com.redhat.darcy.ui.testing.doubles.NeverMetCondition;
import com.redhat.darcy.ui.testing.doubles.NullContext;
import com.redhat.synq.AbstractCondition;
import com.redhat.synq.Condition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class AbstractViewIsLoadedTest {
    @Test(expected = NoRequiredElementsException.class)
    public void shouldThrowNoRequiredElementsExceptionIfCalledWithoutAnyAnnotatedElements() {
        View testView = new AbstractView() {
            Element element = new AlwaysDisplayedLabel();
        };

        testView.isLoaded();
    }
    
    @Test
    public void shouldReturnTrueIfAllRequiredElementsAreDisplayed() {
        View testView = new AbstractView() {
            @Require
            private Element test = new AlwaysDisplayedLabel();
        };
        
        assertTrue("isLoaded should return true if all required elements are displayed.", 
                testView.isLoaded());
    }

    @Test
    public void shouldReturnFalseIfNotAllRequiredElementsAreDisplayed() {
        View testView = new AbstractView() {
            @Require
            private Element displayed = new AlwaysDisplayedLabel();
            @Require
            private Element notDisplayed = new NeverDisplayedElement();
        };
        
        assertFalse("isLoaded should return false if not all required elements are displayed.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnTrueIfRequireAllIsUsedAndAllElementsAreDisplayed() {
        @RequireAll class TestView extends AbstractView {
            private Element displayed = new AlwaysDisplayedLabel();
            private Element displayed2 = new AlwaysDisplayedLabel();
        }
        
        View testView = new TestView();
        
        assertTrue("isLoaded should return true if all required elements are displayed and "
                        + "RequireAll annotation is used.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnFalseIfRequireAllIsUsedAndNotAllElementsAreDisplayed() {
        @RequireAll class TestView extends AbstractView {
            private Element displayed = new AlwaysDisplayedLabel();
            private Element notDisplayed = new NeverDisplayedElement();
        }
        
        View testView = new TestView();
        
        assertFalse("isLoaded should return false if not all required elements are displayed and "
                        + "RequireAll annotation is used.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnTrueIfOnlyElementNotDisplayedIsNotRequired() {
        @RequireAll class TestView extends AbstractView {
            private Element displayed = new AlwaysDisplayedLabel();
            @NotRequired
            private Element notDisplayed = new NeverDisplayedElement();
        }
        
        View testView = new TestView();
        
        assertTrue("isLoaded should return true if only element not displayed is not required when "
                        + "RequireAll annotation is used.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnFalseIfOnlyElementDisplayedIsNotRequired() {
        @RequireAll class TestView extends AbstractView {
            @NotRequired
            private Element displayed = new AlwaysDisplayedLabel();
            private Element notDisplayed = new NeverDisplayedElement();
        }
        
        View testView = new TestView();
        
        assertFalse("isLoaded should return false if only element actually displayed is not required"
                + " when RequireAll annotation is used.", 
                testView.isLoaded());
    }
    
    @Test
    public void shouldAllowBeingOverridden() {
        View testView = new AbstractView() {
            @Require
            private Element displayed = new AlwaysDisplayedLabel();
            
            @Override
            public boolean isLoaded() {
                return super.isLoaded();
            }
        };
        
        assertTrue(testView.isLoaded());
    }
    
    @Test(expected = TestException.class)
    public void shouldPropagateUncheckedExceptions() {
        Element throwsExceptionOnIsDisplayed = mock(Element.class);
        when(throwsExceptionOnIsDisplayed.isDisplayed()).thenThrow(TestException.class);

        View testView = new AbstractView() {
            @Require Element element = throwsExceptionOnIsDisplayed;
        };
        
        testView.setContext(new NullContext());
        testView.isLoaded();
    }

    @Test
    public void shouldFavorIsLoadedOverIsDisplayedIfRequiredFieldIsAView() {
        CustomElement mockElement = mock(CustomElement.class);

        View testView = new AbstractView() {
            @Require
            CustomElement element = mockElement;
        };

        testView.isLoaded();

        verify(mockElement).isLoaded();
        verify(mockElement, never()).isDisplayed();
        verify(mockElement, never()).isPresent();
    }

    interface CustomElement extends View, Element {}

    class TestException extends RuntimeException {}
}
