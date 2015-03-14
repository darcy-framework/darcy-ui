package com.redhat.darcy.ui.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.redhat.darcy.ui.api.elements.Element;

public class ElementIsDisplayedTest {

    @Test
    public void shouldMatchWhenElementIsDisplayed() {
        ElementIsDisplayed<Element> matcher = new ElementIsDisplayed<Element>();
        Element mockElement = mock(Element.class);

        when(mockElement.isDisplayed()).thenReturn(true);

        assertTrue(matcher.matches(mockElement));

    }

    @Test
    public void shouldNotMatchWhenElementIsNotDisplayed() {
        ElementIsDisplayed<Element> matcher = new ElementIsDisplayed<Element>();
        Element mockElement = mock(Element.class);

        when(mockElement.isDisplayed()).thenReturn(false);

        assertFalse(matcher.matches(mockElement));

    }

}
