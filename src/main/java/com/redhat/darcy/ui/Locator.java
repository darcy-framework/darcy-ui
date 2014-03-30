package com.redhat.darcy.ui;

public interface Locator {
    /*
     * <T> List<T> findAll(Class<T> type, Context context);
     * default <T> T findFirst(Class<T> type, Context context) {
     *     return findAll(type, context).get(0);
     * }
     */
    
    <T> T find(Class<T> type, Context context);
}
