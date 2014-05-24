package com.redhat.darcy.ui;

import com.redhat.darcy.ui.elements.Element;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LocatorTransformingElementSelection implements ElementSelection {
    private final ElementSelection elementSelection;
    private final UnaryOperator<Locator> locatorFunction;
    
    public LocatorTransformingElementSelection(ElementSelection elementSelection,
            UnaryOperator<Locator> locatorFunction) {
        Objects.requireNonNull(elementSelection);
        Objects.requireNonNull(locatorFunction);
        
        this.elementSelection = elementSelection;
        this.locatorFunction = locatorFunction;
    }
    
    @Override
    public <T extends Element> T ofType(Class<T> elementType, Locator locator) {
        return elementSelection.ofType(elementType, locatorFunction.apply(locator));
    }
    
    @Override
    public <T extends Element> List<T> listOfType(Class<T> elementType, Locator locator) {
        return elementSelection.listOfType(elementType, locatorFunction.apply(locator));
    }
    
    @Override
    public <T extends Element> T ofType(Class<T> elementType, Locator locator, T implementation) {
        return elementSelection.ofType(elementType, locatorFunction.apply(locator),
                implementation);
    }
    
    @Override
    public <T extends Element> List<T> listOfType(Class<T> elementType, Locator locator,
            Supplier<? extends T> implementation) {
        return elementSelection.listOfType(elementType, locatorFunction.apply(locator),
                implementation);
    }
    
}
