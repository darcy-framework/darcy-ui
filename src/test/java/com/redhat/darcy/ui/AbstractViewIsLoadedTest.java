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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.annotations.Context;
import com.redhat.darcy.ui.annotations.NotRequired;
import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.annotations.RequireAll;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;
import com.redhat.darcy.ui.testing.doubles.NeverDisplayedElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

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
                + "RequireAll annotation is used.", testView.isLoaded());
    }
    
    @Test
    public void shouldReturnFalseIfRequireAllIsUsedAndNotAllElementsAreDisplayed() {
        @RequireAll class TestView extends AbstractView {
            private Element displayed = new AlwaysDisplayedLabel();
            private Element notDisplayed = new NeverDisplayedElement();
        }
        
        View testView = new TestView();
        
        assertFalse("isLoaded should return false if not all required elements are displayed and "
                + "RequireAll annotation is used.", testView.isLoaded());
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
                + "RequireAll annotation is used.", testView.isLoaded());
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
                + " when RequireAll annotation is used.", testView.isLoaded());
    }

    @Test
    public void shouldReturnTrueIfRequireIsSpecifiedForAListWithASingleElementLoaded() {
        class TestView extends AbstractView {
            @Require
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();
                elements.add(new AlwaysDisplayedLabel());
            }
        }

        TestView view = new TestView();

        assertTrue("isLoaded should return true if no specific requirement limit is specified for" +
                   " for a list that has at least a single element loaded.", view.isLoaded());
    }

    @Test
    public void shouldReturnFalseIfRequireIsSpecifiedForAListWithNoElementsLoaded() {
        class TestView extends AbstractView {
            @Require
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();
            }
        }

        TestView view = new TestView();

        assertFalse("isLoaded should return false if a list with no elements loaded is specified" +
                    " required with no requirement limit.", view.isLoaded());

    }

    @Test
    public void shouldReturnTrueIfExactNumberOfElementsSpecifiedInListAreLoaded() {
        class TestView extends AbstractView {
            @Require(exactly = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 5) {
                    elements.add(new AlwaysDisplayedLabel());
                }
            }
        }

        TestView view = new TestView();

        assertTrue("Should return true if an exact number of elements in a list are loaded.",
                   view.isLoaded());
    }

    @Test
    public void shouldReturnFalseIfExactNumberOfElementsSpecifiedInAListAreNotLoaded() {
        class TestView extends AbstractView {
            @Require(exactly = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 4) {
                    elements.add(new AlwaysDisplayedLabel());
                }

                elements.add(new NeverDisplayedElement());
            }
        }

        TestView view = new TestView();

        assertFalse("isLoaded should return false if all required elements are not loaded.",
                view.isLoaded());
    }

    @Test
    public void shouldReturnTrueIfAtLeastAsManyElementsNeededAreLoaded() {
        class TestView extends AbstractView {
            @Require(atLeast = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 6) {
                    elements.add(new AlwaysDisplayedLabel());
                }

                while (elements.size() < 10) {
                    elements.add(new NeverDisplayedElement());
                }
            }
        }

        TestView view = new TestView();

        assertTrue("Should return true if at least 5 elements are loaded in the list.",
                   view.isLoaded());
    }

    @Test
    public void shouldReturnTrueIfAsManyElementsNeededAreLoaded() {
        class TestView extends AbstractView {
            @Require(atLeast = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 5) {
                    elements.add(new AlwaysDisplayedLabel());
                }

                while (elements.size() < 10) {
                    elements.add(new NeverDisplayedElement());
                }
            }
        }

        TestView view = new TestView();

        assertTrue("Should return true if at least 5 elements are loaded in the list.",
                view.isLoaded());
    }

    @Test
    public void shouldReturnFalseIfLessElementsThanSpecifiedAreLoaded() {
        class TestView extends AbstractView {
            @Require(atLeast = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 4) {
                    elements.add(new AlwaysDisplayedLabel());
                }

                while (elements.size() < 10) {
                    elements.add(new NeverDisplayedElement());
                }

                elements.add(new NeverDisplayedElement());
            }
        }

        TestView view = new TestView();

        assertFalse("isLoaded should return false if atLeast was specified and not enough elements" +
                    " are loaded.", view.isLoaded());
    }

    @Test
    public void shouldReturnTrueIfAtMostAsManyElementsNeededAreLoaded() {
        class TestView extends AbstractView {
            @Require(atMost = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 4) {
                    elements.add(new AlwaysDisplayedLabel());
                }
            }
        }

        TestView view = new TestView();

        assertTrue("Should return true if at most 4 elements are loaded in the list.",
                view.isLoaded());
    }

    @Test
    public void shouldReturnTrueIfExactlyAsManyElementsNeededSpecifiedWithAtMostAreLoaded() {
        class TestView extends AbstractView {
            @Require(atMost = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 5) {
                    elements.add(new AlwaysDisplayedLabel());
                }
            }
        }

        TestView view = new TestView();

        assertTrue("Should return true if at most 5 elements are loaded in the list.",
                view.isLoaded());

    }

    @Test
    public void shouldReturnFalseIfMoreElementsThanSpecifiedWithAtMostAreLoaded() {
        class TestView extends AbstractView {
            @Require(atMost = 5)
            private List<Element> elements;

            public TestView() {
                elements = new ArrayList<Element>();

                while (elements.size() < 6) {
                    elements.add(new AlwaysDisplayedLabel());
                }
            }
        }

        TestView view = new TestView();

        assertFalse("Should return false if 6 elements are loaded in the list.",
                view.isLoaded());
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
        
        testView.isLoaded();
    }

    @Test
    public void shouldReturnTrueIfRequiredFieldIsAViewThatIsLoaded() {
        View mockView = mock(View.class);
        when(mockView.isLoaded()).thenReturn(true);

        View testView = new AbstractView() {
            @Require
            View view = mockView;
        };

        assertTrue("isLoaded should check View fields for isLoaded.", testView.isLoaded());

        verify(mockView).isLoaded();
    }

    @Test
    public void shouldReturnFalseIfRequiredFieldIsAViewThatIsNotLoaded() {
        View mockView = mock(View.class);
        when(mockView.isLoaded()).thenReturn(false);

        View testView = new AbstractView() {
            @Require
            View view = mockView;
        };

        assertFalse("isLoaded should check View fields for isLoaded.", testView.isLoaded());

        verify(mockView).isLoaded();
    }

    @Test
    public void shouldReturnTrueIfRequiredFieldIsAFindableThatIsPresent() {
        Findable mockFindable = mock(Findable.class);
        when(mockFindable.isPresent()).thenReturn(true);

        View testView = new AbstractView() {
            @Require
            Findable findable = mockFindable;
        };

        assertTrue("isLoaded should check Findable fields for isPresent if they do not implement " +
                "View or Element.", testView.isLoaded());
    }

    @Test
    public void shouldReturnFalseIfRequiredFieldIsAFindableThatIsNotPresent() {
        Findable mockFindable = mock(Findable.class);
        when(mockFindable.isPresent()).thenReturn(false);

        View testView = new AbstractView() {
            @Require
            Findable findable = mockFindable;
        };

        assertFalse("isLoaded should check Findable fields for isPresent if they do not implement" +
                "View or Element.", testView.isLoaded());
    }

    @Test(expected = NoRequiredElementsException.class)
    public void shouldIgnoreFieldsAnnotatedWithContext() {
        @RequireAll class TestView extends AbstractView {
            @Context
            Element element = mock(Element.class);
        };

        TestView testView = new TestView();
        testView.isLoaded();
    }

    @Test(expected = NoRequiredElementsException.class)
    public void shouldNotConsiderIrrelevantFieldsAsAbleToBeRequired() {
        @RequireAll class TestView extends AbstractView {
            String aString;
        }

        TestView testView = new TestView();

        testView.isLoaded();
    }

    @Test(expected = NoRequiredElementsException.class)
    public void shouldReconsiderEmptyConditionsListIfFieldIsAmbiguous() {
        @RequireAll class TestView extends AbstractView {
            List<String> stringList;
        };

        TestView testView = new TestView();
        testView.isLoaded();
    }

    class TestException extends RuntimeException {}
}
