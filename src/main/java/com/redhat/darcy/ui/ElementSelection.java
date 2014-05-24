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
 * Fluently retrieve an element reference or a element list reference of a specified type.
 */
public interface ElementSelection {
    /**
     * Retrieve a reference to a single element of some type. The returned element reference may or 
     * may not be found within the context. You can safely determine whether an element was found by
     * calling {@link com.redhat.darcy.ui.elements.Element#isDisplayed()}.
     * @param elementType
     * @param locator
     * @return
     */
    <T extends Element> T ofType(Class<T> elementType, Locator locator);
    
    /**
     * Retrieve a list of elements. Only found elements will be contained within the list. List may
     * only reflect elements found at the moment a method on the list is called, or it may reflect
     * elements found at the time of retrieving the list.
     * @param elementType
     * @param locator
     * @return
     */
    <T extends Element> List<T> listOfType(Class<T> elementType, Locator locator);
    
    <T extends Element> T ofType(Class<T> elementType, Locator locator, T implementation);
    <T extends Element> List<T> listOfType(Class<T> elementType, Locator locator, 
            Supplier<? extends T> implementation);
    
    default Element element(Locator locator) {
        return ofType(Element.class, locator);
    }
    
    default List<Element> elements(Locator locator) {
        return listOfType(Element.class, locator);
    }
    
    default TextInput textInput(Locator locator) {
        return ofType(TextInput.class, locator);
    }
    default List<TextInput> textInputs(Locator locator) {
        return listOfType(TextInput.class, locator);
    }
    
    default Button button(Locator locator) {
        return ofType(Button.class, locator);
    }
    
    default List<Button> buttons(Locator locator) {
        return listOfType(Button.class, locator);
    }
    
    default Label label(Locator locator) {
        return ofType(Label.class, locator);
    }
    
    default List<Label> labels(Locator locator) {
        return listOfType(Label.class, locator);
    }
    
    default Link link(Locator locator) {
        return ofType(Link.class, locator);
    }
    
    default List<Link> links(Locator locator) {
        return listOfType(Link.class, locator);
    }
    
    default Select select(Locator locator) {
        return ofType(Select.class, locator);
    }
    
    default List<Select> selects(Locator locator) {
        return listOfType(Select.class, locator);
    }
}
