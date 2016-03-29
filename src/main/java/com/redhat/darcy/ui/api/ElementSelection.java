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

import com.redhat.darcy.ui.api.elements.Button;
import com.redhat.darcy.ui.api.elements.Checkbox;
import com.redhat.darcy.ui.api.elements.DateInput;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.FileSelect;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.api.elements.Link;
import com.redhat.darcy.ui.api.elements.SelectOption;
import com.redhat.darcy.ui.api.elements.MultiSelect;
import com.redhat.darcy.ui.api.elements.Radio;
import com.redhat.darcy.ui.api.elements.Select;
import com.redhat.darcy.ui.api.elements.Text;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.ui.internal.ChainedViewFactory;
import com.redhat.darcy.ui.internal.NestedViewFactory;

import java.util.List;

/**
 * A {@link Selection} for {@link com.redhat.darcy.ui.api.elements.Element}s.
 *
 * @see ElementContext
 */
public interface ElementSelection extends Selection {
    /**
     * Retrieve a reference to a single element of some type. The returned element reference may or
     * may not be found within the context. This is to say that if an element is <em>not found</em>
     * by the given locator, no exception will be thrown, and an element of that type will still be
     * returned. An exception will be thrown, however, if you try to act on that element in a way
     * that requires its presence. You can safely determine whether an element was found by calling
     * {@link com.redhat.darcy.ui.api.elements.Element#isDisplayed()}.
     *
     * @param elementType The type of element reference to retrieve. Whether or not the automation
     * library enforces the found element actually be of the desired type is implementation
     * specific.
     * @param locator
     * @return
     */
    <T extends Element> T elementOfType(Class<T> elementType, Locator locator);

    /**
     * Retrieve a list of elements. Only found elements will be contained within the list. List may
     * only reflect elements found at the moment a method on the list is called, or it may reflect
     * elements found at the time of retrieving the list.
     *
     * @param elementType The type of element reference to retrieve. Whether or not the automation
     * library enforces the found element actually be of the desired type is implementation
     * specific.
     * @param locator
     * @return
     */
    <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator);

    <T extends View> T viewOfType(ChainedViewFactory<T> elementCtor, Locator locator);

    <T extends View> List<T> viewsOfType(NestedViewFactory<T> elementCtor, Locator locator);

    default Element element(Locator locator) {
        return elementOfType(Element.class, locator);
    }

    default List<Element> elements(Locator locator) {
        return elementsOfType(Element.class, locator);
    }

    default TextInput textInput(Locator locator) {
        return elementOfType(TextInput.class, locator);
    }

    default List<TextInput> textInputs(Locator locator) {
        return elementsOfType(TextInput.class, locator);
    }

    default Button button(Locator locator) {
        return elementOfType(Button.class, locator);
    }

    default List<Button> buttons(Locator locator) {
        return elementsOfType(Button.class, locator);
    }

    default Label label(Locator locator) {
        return elementOfType(Label.class, locator);
    }

    default List<Label> labels(Locator locator) {
        return elementsOfType(Label.class, locator);
    }

    default Link link(Locator locator) {
        return elementOfType(Link.class, locator);
    }

    default List<Link> links(Locator locator) {
        return elementsOfType(Link.class, locator);
    }

    default Select<SelectOption> select(Locator locator) {
        return elementOfType(Select.class, locator);
    }

    default List<Select> selects(Locator locator) {
        return elementsOfType(Select.class, locator);
    }

    default Radio radio(Locator locator) {
        return elementOfType(Radio.class, locator);
    }

    default List<Radio> radios(Locator locator) {
        return elementsOfType(Radio.class, locator);
    }

    default Text text(Locator locator) {
        return elementOfType(Text.class, locator);
    }

    default List<Text> texts(Locator locator) {
        return elementsOfType(Text.class, locator);
    }

    default FileSelect fileSelect(Locator locator) {
        return elementOfType(FileSelect.class, locator);
    }

    default List<FileSelect> fileSelects(Locator locator) {
        return elementsOfType(FileSelect.class, locator);
    }

    default Checkbox checkbox(Locator locator) {
        return elementOfType(Checkbox.class, locator);
    }

    default List<Checkbox> checkboxes(Locator locator) {
        return elementsOfType(Checkbox.class, locator);
    }

    default DateInput dateInput(Locator locator) {
        return elementOfType(DateInput.class, locator);
    }

    default List<DateInput> dateInputs(Locator locator) {
        return elementsOfType(DateInput.class, locator);
    }

    default MultiSelect multiSelect(Locator locator) {
        return elementOfType(MultiSelect.class, locator);
    }

    default List<MultiSelect> multiSelects(Locator locator) {
        return elementsOfType(MultiSelect.class, locator);
    }

}
