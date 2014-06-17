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

import com.redhat.darcy.ui.elements.Button;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.Label;
import com.redhat.darcy.ui.elements.Link;
import com.redhat.darcy.ui.elements.Select;
import com.redhat.darcy.ui.elements.TextInput;

import java.util.List;
import java.util.function.Supplier;

/**
 * A {@link Selection} for {@link com.redhat.darcy.ui.elements.Element}s.
 *
 * @see ElementContext
 */
public interface ElementSelection extends Selection {
    /**
     * Retrieve a reference to a single element of some type. The returned element reference may or 
     * may not be found within the context. You can safely determine whether an element was found by
     * calling {@link com.redhat.darcy.ui.elements.Element#isDisplayed()}.
     * @param elementType
     * @param locator
     * @return
     */
    <T extends Element> T elementOfType(Class<T> elementType, Locator locator);
    
    /**
     * Retrieve a list of elements. Only found elements will be contained within the list. List may
     * only reflect elements found at the moment a method on the list is called, or it may reflect
     * elements found at the time of retrieving the list.
     * @param elementType
     * @param locator
     * @return
     */
    <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator);
    
    <T extends Element> T elementOfType(Class<T> elementType, Locator locator, T implementation);
    <T extends Element> List<T> elementsOfType(Class<T> elementType, Locator locator,
            Supplier<? extends T> implementation);
    
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
    
    default Select select(Locator locator) {
        return elementOfType(Select.class, locator);
    }
    
    default List<Select> selects(Locator locator) {
        return elementsOfType(Select.class, locator);
    }
}
