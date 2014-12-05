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

import static com.redhat.darcy.ui.matchers.DarcyMatchers.present;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.api.elements.Button;
import com.redhat.darcy.ui.api.elements.HasAttributes;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class ByIdOfTest {
    @Test
    public void shouldUseIdOfElementForSubsequentLookups() {
        ByTest.FindsByAll mockContext = mock(ByTest.FindsByAll.class);
        ButtonWithAttributes mockButton = mock(ButtonWithAttributes.class, "genericallyFound");
        Button mockButtonFoundById = mock(Button.class, "specificallyFound");

        // Setup context to find the element by less specific locator
        when(mockContext.findByTextContent(Button.class, "test")).thenReturn(mockButton);
        when(mockContext.findById(Button.class, "theId")).thenReturn(mockButtonFoundById);

        // Configure element to have id and be found; once it is found it's id will be used instead
        when(mockButton.getAttribute("id")).thenReturn("theId");
        when(mockButton.isPresent()).thenReturn(true);

        when(mockButtonFoundById.isPresent()).thenReturn(true);

        By.ByIdOf byIdOf = By.idOf(By.textContent("test"));

        Button button = byIdOf.find(Button.class, mockContext);

        assertTrue(button.isPresent());
        assertSame(mockButtonFoundById, ((WrapsElement) button).getWrappedElement());
    }

    @Test
    public void shouldIdsOfListOfElementsForSubsequentLookups() {
        ByTest.FindsByAll mockContext = mock(ByTest.FindsByAll.class);

        Label labelWithId1 = new LabelWithId("id1");
        Label labelWithId2 = new LabelWithId("id2");
        Label labelWithId3 = new LabelWithId("id3");

        Label labelFoundById1 = new AlwaysDisplayedLabel();
        Label labelFoundById2 = new AlwaysDisplayedLabel();
        Label labelFoundById3 = new AlwaysDisplayedLabel();

        // Setup context to find the elements by less specific locator
        when(mockContext.findAllByTextContent(Label.class, "test"))
                .thenReturn(Arrays.asList(labelWithId1, labelWithId2, labelWithId3));

        // Setup context to find the elements by the more specific locator
        when(mockContext.findById(Label.class, "id1")).thenReturn(labelFoundById1);
        when(mockContext.findById(Label.class, "id2")).thenReturn(labelFoundById2);
        when(mockContext.findById(Label.class, "id3")).thenReturn(labelFoundById3);

        By.ByIdOf byIdOf = By.idOf(By.textContent("test"));

        List<Label> labels = byIdOf.findAll(Label.class, mockContext);

        assertThat(labels, contains(present(), present(), present()));
        assertThat(labels, contains(
                sameInstance(labelFoundById1),
                sameInstance(labelFoundById2),
                sameInstance(labelFoundById3)));
    }

    interface ButtonWithAttributes extends Button, HasAttributes {}

    class LabelWithId extends AlwaysDisplayedLabel implements HasAttributes {
        private final String id;

        LabelWithId(String id) {
            this.id = id;
        }

        @Override
        public String getAttribute(String attribute) {
            if ("id".equalsIgnoreCase(attribute)) {
                return id;
            }

            return null;
        }
    }
}
