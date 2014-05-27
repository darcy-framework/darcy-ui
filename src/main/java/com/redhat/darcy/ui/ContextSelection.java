package com.redhat.darcy.ui;

import java.util.List;

/**
 * @see ParentContext
 */
public interface ContextSelection {
    /**
     * Retrieve a reference to another context found by the current. This context may be a "child"
     * or a "sibling," this is up to the implementation.
     * @param contextType
     * @param locator
     * @return
     */
    <T extends Context> T ofType(Class<T> contextType, Locator locator);
    
    /**
     * Retrieve a reference to a list of other context found by the current. These contexts may be 
     * "children" or "siblings," this is up to the implementation.
     * @param contextType
     * @param locator
     * @return
     */
    <T extends Context> List<T> listOfType(Class<T> contextType, Locator locator);
}
