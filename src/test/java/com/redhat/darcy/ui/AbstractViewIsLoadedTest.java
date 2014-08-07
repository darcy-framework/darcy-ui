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

import com.redhat.darcy.ui.annotations.NotRequired;
import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.annotations.RequireAll;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.AlwaysMetCondition;
import com.redhat.darcy.ui.testing.doubles.NeverDisplayedElement;
import com.redhat.darcy.ui.testing.doubles.NeverMetCondition;
import com.redhat.darcy.ui.testing.doubles.NullContext;
import com.redhat.synq.AbstractCondition;
import com.redhat.synq.Condition;

import org.junit.Test;

@SuppressWarnings("unused")
public class AbstractViewIsLoadedTest {
    @Test(expected = NullContextException.class)
    public void shouldThrowNullContextExceptionIfCalledBeforeSettingAContext() {
        View testView = new AbstractView() {};
        
        testView.isLoaded();
    }
    
    @Test
    public void shouldReturnTrueIfAllRequiredElementsAreDisplayed() {
        View testView = new AbstractView() {
            @Require
            private Element test = new AlwaysDisplayedLabel();
        };
        
        testView.setContext(new NullContext());
        
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
        
        testView.setContext(new NullContext());
        
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
        
        testView.setContext(new NullContext());
        
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
        
        testView.setContext(new NullContext());
        
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
        
        testView.setContext(new NullContext());
        
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
        
        testView.setContext(new NullContext());
        
        assertFalse("isLoaded should return false if only element actually displayed is not required"
                + " when RequireAll annotation is used.", 
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnTrueIfCustomLoadConditionUsedThatIsTrue() {
        View testView = new AbstractView() {
            @Override
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        };
        
        testView.setContext(new NullContext());
        
        assertTrue("isLoaded should return true if only load condition is provided by override to "
                + "Callable<Boolean> loadCondition() and the callable returns true.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnFalseIfCustomLoadConditionUsedThatIsFalse() {
        View testView = new AbstractView() {
            @Override
            protected Condition<?> loadCondition() {
                return new NeverMetCondition<>();
            }
        };
        
        testView.setContext(new NullContext());
        
        assertFalse("isLoaded should return false if only load condition is provided by override to "
                + "Callable<Boolean> loadCondition() and the callable returns false.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnTrueIfAllConditionsMetViaLoadConditionAndRequiredAnnotations() {
        View testView = new AbstractView() {
            @Require
            private Element displayed = new AlwaysDisplayedLabel();
            
            @Override
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        };
        
        testView.setContext(new NullContext());
        
        assertTrue("isLoaded should return true if all required elements are displayed and custom "
                + "load condition is met.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnFalseIfRequiredElementIsDisplayedButLoadConditionNotMet() {
        View testView = new AbstractView() {
            @Require
            private Element displayed = new AlwaysDisplayedLabel();
            
            @Override
            protected Condition<?> loadCondition() {
                return new NeverMetCondition<>();
            }
        };
        
        testView.setContext(new NullContext());
        
        assertFalse("isLoaded should return false if all required elements are displayed but custom "
                + "load condition is not met.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldReturnFalseIfLoadConditionIsMetButRequiredElementIsNotDisplayed() {
        View testView = new AbstractView() {
            @Require
            private Element notDisplayed = new NeverDisplayedElement();
            
            @Override
            protected Condition<?> loadCondition() {
                return new AlwaysMetCondition<>();
            }
        };
        
        testView.setContext(new NullContext());
        
        assertFalse("isLoaded should return false if load condition is met but not all required "
                + "elements are displayed.",
                testView.isLoaded());
    }
    
    @Test(expected = TestException.class)
    public void shouldPropagateUncheckedExceptions() {
        View testView = new AbstractView() {
            @Override
            protected Condition<?> loadCondition() {
                return new AbstractCondition<Void>() {
                    @Override public boolean isMet() { throw new TestException(); }
                    @Override public Void lastResult() { return null; }
                };
            }
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

        testView.setContext(new NullContext());
        testView.isLoaded();

        verify(mockElement).isLoaded();
        verify(mockElement, never()).isDisplayed();
        verify(mockElement, never()).isPresent();
    }

    interface CustomElement extends View, Element {}

    class TestException extends RuntimeException {}
}
