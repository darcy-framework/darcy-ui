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

import com.redhat.darcy.ui.api.HasElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.WrapsElement;
import com.redhat.darcy.ui.internal.ElementHandler;
import com.redhat.darcy.ui.internal.ElementListHandler;
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Static factories for the fundamental UI elements. Specifically, these return proxy instances of
 * those elements, so that they may be defined as instance fields in a View that may not yet have
 * a context with which to retrieve element references. This elements can be assigned a context
 * after instantiation.
 *
 * @see com.redhat.darcy.ui.AbstractView
 * @see com.redhat.darcy.ui.api.HasElementContext
 * @see com.redhat.darcy.ui.internal.ElementHandler
 *
 */
public abstract class Elements {
    /**
     * Looks up a automation-library-specific implementation for that element type, assuming an
     * implementation is registered for that class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Element> T element(Class<T> type, Locator locator) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: " + type);
        }

        InvocationHandler invocationHandler = new ElementHandler(type, locator);

        return (T) Proxy.newProxyInstance(Elements.class.getClassLoader(),
                new Class[] { type, HasElementContext.class, WrapsElement.class},
                invocationHandler);
    }
    /**
     * Looks up a automation-library-specific implementation for that element type, assuming an
     * implementation is registered for that class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Element> List<T> elements(Class<T> type, Locator locator) {
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Element type must be an interface, was: " + type);
        }

        InvocationHandler invocationHandler = new ElementListHandler(type, locator);

        return (List<T>) Proxy.newProxyInstance(Elements.class.getClassLoader(),
                new Class[] { List.class, HasElementContext.class },
                invocationHandler);
    }

    public static Element element(Locator locator) {
        return element(Element.class, locator);
    }

    public static List<Element> elements(Locator locator) {
        return elements(Element.class, locator);
    }

    public static TextInput textInput(Locator locator) {
        return element(TextInput.class, locator);
    }

    public static List<TextInput> textInputs(Locator locator) {
        return elements(TextInput.class, locator);
    }

    public static Button button(Locator locator) {
        return element(Button.class, locator);
    }

    public static List<Button> buttons(Locator locator) {
        return elements(Button.class, locator);
    }

    public static Link link(Locator locator) {
        return element(Link.class, locator);
    }

    public static List<Link> links(Locator locator) {
        return elements(Link.class, locator);
    }

    public static Label label(Locator locator) {
        return element(Label.class, locator);
    }

    public static List<Label> labels(Locator locator) {
        return elements(Label.class, locator);
    }

    public static Select<SelectOption> select(Locator locator) {
        return element(Select.class, locator);
    }

    public static List<Select> selects(Locator locator) {
        return elements(Select.class, locator);
    }

    public static Radio radio(Locator locator) {
        return element(Radio.class, locator);
    }

    public static List<Radio> radios(Locator locator) {
        return elements(Radio.class, locator);
    }

    public static Checkbox checkbox(Locator locator) {
        return element(Checkbox.class, locator);
    }

    public static List<Checkbox> checkboxes(Locator locator) {
        return elements(Checkbox.class, locator);
    }

    public static Text text(Locator locator) {
        return element(Text.class, locator);
    }

    public static List<Text> texts(Locator locator) {
        return elements(Text.class, locator);
    }

    public static FileSelect fileSelect(Locator locator) {
        return element(FileSelect.class, locator);
    }

    public static List<FileSelect> fileSelects(Locator locator) {
        return elements(FileSelect.class, locator);
    }

    public static DateInput dateInput(Locator locator) {
        return element(DateInput.class, locator);
    }

    public static List<DateInput> dateInputs(Locator locator) {
        return elements(DateInput.class, locator);
    }

    public static MultiSelect multiSelect(Locator locator) {
        return element(MultiSelect.class, locator);
    }

    public static List<MultiSelect> multiSelects(Locator locator) {
        return elements(MultiSelect.class, locator);
    }
}
