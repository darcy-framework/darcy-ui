package com.redhat.darcy.ui;

import java.util.List;

/**
 * A locator is a strategy for finding an instance (or List of instances) that implement some class,
 * given some {@link Context}.
 */
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
     * By default, if there are multiple instances that would satisfy this locator, then this method 
     * picks one of them. The instance picked is up to implementation.
     * 
     * @param type
     * @param context
     * @return
     */
    default <T> T find(Class<T> type, Context context) {
        List<T> found = findAll(type, context);
        
        if (found.isEmpty()) {
            throw new NotFoundException(type, this);
        }
        
        return found.get(0);
    }
}
