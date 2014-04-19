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

import com.redhat.darcy.ui.elements.Element;

import org.junit.Test;

import java.util.concurrent.Callable;

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
            public Callable<Boolean> loadCondition() {
                return () -> true;
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
            public Callable<Boolean> loadCondition() {
                return () -> false;
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
            public Callable<Boolean> loadCondition() {
                return () -> true;
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
            public Callable<Boolean> loadCondition() {
                return () -> false;
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
            public Callable<Boolean> loadCondition() {
                return () -> true;
            }
        };
        
        testView.setContext(new NullContext());
        
        assertFalse("isLoaded should return false if load condition is met but not all required "
                + "elements are displayed.",
                testView.isLoaded());
    }
    
    @Test
    public void shouldConsiderALoadConditionNotMetIfItThrowsAnException() {
        View testView = new AbstractView() {
            @Override
            public Callable<Boolean> loadCondition() {
                return () -> {
                    throw new Exception("You should see this logged but it should not fail the "
                            + "test");
                };
            }
        };
        
        testView.setContext(new NullContext());
        
        assertFalse("isLoaded should return false if load condition throws an exception.",
                testView.isLoaded());
    }
}
