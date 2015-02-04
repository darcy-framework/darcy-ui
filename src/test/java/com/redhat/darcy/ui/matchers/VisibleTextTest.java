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

package com.redhat.darcy.ui.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.elements.Text;

import org.hamcrest.Matchers;
import org.junit.Test;

public class VisibleTextTest {
    @Test
    public void shouldMatchVisibleElements() {
        VisibleText<Text> matcher = new VisibleText<>(Matchers.anything());

        Text mockText = mock(Text.class);
        when(mockText.isDisplayed()).thenReturn(true);

        assertTrue(matcher.matches(mockText));
    }

    @Test
    public void shouldNotMatchNotVisibleElements() {
        VisibleText<Text> matcher = new VisibleText<>(Matchers.anything());

        Text mockText = mock(Text.class);
        when(mockText.isDisplayed()).thenReturn(false);

        assertFalse(matcher.matches(mockText));
    }

    @Test
    public void shouldMatchTextAgainstProvidedMatcher() {
        VisibleText<Text> matcher = new VisibleText<>(Matchers.containsString("is a test"));

        Text mockText = mock(Text.class);
        when(mockText.isDisplayed()).thenReturn(true);
        when(mockText.getText()).thenReturn("This is a test.");

        assertTrue(matcher.matches(mockText));
    }
}
