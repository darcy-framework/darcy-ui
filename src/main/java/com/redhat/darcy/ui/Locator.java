package com.redhat.darcy.ui;

import java.util.List;

public interface Locator {
    /**
     * Returns a list of all the elements found by this locator, of the given type, in the given
     * context.
     * 
     * @param type
     * @param context
     * @return
     */
    <T> List<T> findAll(Class<T> type, Context context);
    
    /**
     * Returns one element found by this locator, of the given type, in the given context.
     * <P>
     * By default, if there are multiple elements with the same conditions, then this method picks
     * the first in the list. The order is dependent on the implementation.
     * 
     * @param type
     * @param context
     * @return
     */
    default <T> T find(Class<T> type, Context context) {
        List<T> found = findAll(type, context);
        
        if (found.isEmpty()) {
            // FIXME: Throw some exception here
            return null;
        }
        
        return found.get(0);
    }
}
