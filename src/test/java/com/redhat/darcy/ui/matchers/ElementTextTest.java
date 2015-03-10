package com.redhat.darcy.ui.matchers;

import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.redhat.darcy.ui.api.elements.Text;

public class ElementTextTest {

    @Test
    public void shouldMatchPresentElements() {
        ElementText<Text> matcher = new ElementText<>(anything());
        Text mockText = mock(Text.class);

        when(mockText.isPresent()).thenReturn(true);

        assertTrue(matcher.matches(mockText));

    }

    @Test
    public void shouldNotMatchNotPresentElements() {
        ElementText<Text> matcher = new ElementText<>(anything());
        Text mockText = mock(Text.class);

        when(mockText.isPresent()).thenReturn(false);

        assertFalse(matcher.matches(mockText));

    }

    @Test
    public void shouldMatchProvidedElementText() {
        ElementText<Text> matcher = new ElementText<>(Matchers.containsString("some element text"));
        Text mockText = mock(Text.class);

        when(mockText.isPresent()).thenReturn(true);
        when(mockText.getText()).thenReturn("some element text");

        assertTrue(matcher.matches(mockText));
    }

    @Test
    public void shouldNotMatchDifferentElementText() {
        ElementText<Text> matcher = new ElementText<>(Matchers.containsString("some element text"));
        Text mockText = mock(Text.class);

        when(mockText.isPresent()).thenReturn(true);
        when(mockText.getText()).thenReturn("some different element text");

        assertFalse(matcher.matches(mockText));
    }

}
