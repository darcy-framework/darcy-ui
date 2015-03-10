package com.redhat.darcy.ui.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.redhat.darcy.ui.api.elements.Element;

public class FindableIsPresentTest {

    @Test
    public void shouldMatchWhenFindableIsPresent() {
        FindableIsPresent<Element> matcher = new FindableIsPresent<Element>();
        Element mockElement = mock(Element.class);

        when(mockElement.isPresent()).thenReturn(true);

        assertTrue(matcher.matches(mockElement));
    }

    @Test
    public void shouldNotMatchWhenFindableIsNotPresent() {
        FindableIsPresent<Element> matcher = new FindableIsPresent<Element>();
        Element mockElement = mock(Element.class);

        when(mockElement.isPresent()).thenReturn(false);

        assertFalse(matcher.matches(mockElement));
    }

}
