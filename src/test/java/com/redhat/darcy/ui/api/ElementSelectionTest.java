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

package com.redhat.darcy.ui.api;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.api.elements.Button;
import com.redhat.darcy.ui.api.elements.Checkbox;
import com.redhat.darcy.ui.api.elements.DateInput;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.FileSelect;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.api.elements.Link;
import com.redhat.darcy.ui.api.elements.MultiSelect;
import com.redhat.darcy.ui.api.elements.Radio;
import com.redhat.darcy.ui.api.elements.Select;
import com.redhat.darcy.ui.api.elements.Text;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.internal.ChainedViewFactory;
import com.redhat.darcy.ui.internal.NestedViewFactory;
import com.redhat.darcy.ui.testing.doubles.AlwaysDisplayedLabel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class ElementSelectionTest {
    private ElementSelection selection = spy(new ElementSelection() {
        @Override
        public <T extends Element> T elementOfType(Class<T> elementType, Locator locator) {
            return null;
        }

        @Override
        public <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator) {
            return null;
        }

        @Override
        public <T extends View> T viewOfType(ChainedViewFactory<T> elementCtor, Locator locator) {
            return null;
        }

        @Override
        public <T extends View> List<T> viewsOfType(NestedViewFactory<T> elementCtor, Locator locator) {
            return null;
        }
    });

    @Test
    public void shouldSelectAnElement() {
        Element shouldFind = new AlwaysDisplayedLabel();
        when(selection.elementOfType(Element.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.element(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfElements() {
        List<Element> shouldFind = Arrays.asList(new AlwaysDisplayedLabel());
        when(selection.elementsOfType(Element.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.elements(By.id("test")));
    }

    @Test
    public void shouldSelectAButton() {
        Button shouldFind = mock(Button.class);
        when(selection.elementOfType(Button.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.button(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfButtons() {
        List<Button> shouldFind = Arrays.asList(mock(Button.class));
        when(selection.elementsOfType(Button.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.buttons(By.id("test")));
    }

    @Test
    public void shouldSelectACheckbox() {
        Checkbox shouldFind = mock(Checkbox.class);
        when(selection.elementOfType(Checkbox.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.checkbox(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfCheckboxes() {
        List<Checkbox> shouldFind = Arrays.asList(mock(Checkbox.class));
        when(selection.elementsOfType(Checkbox.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.checkboxes(By.id("test")));
    }

    @Test
    public void shouldSelectADateInput() {
        DateInput shouldFind = mock(DateInput.class);
        when(selection.elementOfType(DateInput.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.dateInput(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfDateInputs() {
        List<DateInput> shouldFind = Arrays.asList(mock(DateInput.class));
        when(selection.elementsOfType(DateInput.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.dateInputs(By.id("test")));
    }

    @Test
    public void shouldSelectAFileSelect() {
        FileSelect shouldFind = mock(FileSelect.class);
        when(selection.elementOfType(FileSelect.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.fileSelect(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfFileSelects() {
        List<FileSelect> shouldFind = Arrays.asList(mock(FileSelect.class));
        when(selection.elementsOfType(FileSelect.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.fileSelects(By.id("test")));
    }

    @Test
    public void shouldSelectALabel() {
        Label shouldFind = mock(Label.class);
        when(selection.elementOfType(Label.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.label(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfLabels() {
        List<FileSelect> shouldFind = Arrays.asList(mock(FileSelect.class));
        when(selection.elementsOfType(FileSelect.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.fileSelects(By.id("test")));
    }

    @Test
    public void shouldSelectALink() {
        Link shouldFind = mock(Link.class);
        when(selection.elementOfType(Link.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.link(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfLinks() {
        List<Link> shouldFind = Arrays.asList(mock(Link.class));
        when(selection.elementsOfType(Link.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.links(By.id("test")));
    }

    @Test
    public void shouldSelectAMultiSelect() {
        MultiSelect shouldFind = mock(MultiSelect.class);
        when(selection.elementOfType(MultiSelect.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.multiSelect(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfMultiSelects() {
        List<MultiSelect> shouldFind = Arrays.asList(mock(MultiSelect.class));
        when(selection.elementsOfType(MultiSelect.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.multiSelects(By.id("test")));
    }

    @Test
    public void shouldSelectARadio() {
        Radio shouldFind = mock(Radio.class);
        when(selection.elementOfType(Radio.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.radio(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfRadios() {
        List<Radio> shouldFind = Arrays.asList(mock(Radio.class));
        when(selection.elementsOfType(Radio.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.radios(By.id("test")));
    }

    @Test
    public void shouldSelectASelect() {
        Select shouldFind = mock(Select.class);
        when(selection.elementOfType(Select.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.select(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfSelects() {
        List<Select> shouldFind = Arrays.asList(mock(Select.class));
        when(selection.elementsOfType(Select.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.selects(By.id("test")));
    }

    @Test
    public void shouldSelectAText() {
        Text shouldFind = mock(Text.class);
        when(selection.elementOfType(Text.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.text(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfTexts() {
        List<Text> shouldFind = Arrays.asList(mock(Text.class));
        when(selection.elementsOfType(Text.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.texts(By.id("test")));
    }

    @Test
    public void shouldSelectATextInput() {
        TextInput shouldFind = mock(TextInput.class);
        when(selection.elementOfType(TextInput.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.textInput(By.id("test")));
    }

    @Test
    public void shouldSelectAListOfTextInputs() {
        List<TextInput> shouldFind = Arrays.asList(mock(TextInput.class));
        when(selection.elementsOfType(TextInput.class, By.id("test"))).thenReturn(shouldFind);

        assertSame(shouldFind, selection.textInputs(By.id("test")));
    }
}
