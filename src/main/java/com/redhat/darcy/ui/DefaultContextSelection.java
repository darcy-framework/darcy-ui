package com.redhat.darcy.ui;

import java.util.List;

public class DefaultContextSelection implements ContextSelection {
    private final ParentContext parentContext;
    
    public DefaultContextSelection(ParentContext parentContext) {
        this.parentContext = parentContext;
    }
    
    @Override
    public <T extends Context> T ofType(Class<T> contextType, Locator locator) {
        return locator.find(contextType, parentContext);
    }
    
    @Override
    public <T extends Context> List<T> listOfType(Class<T> contextType, Locator locator) {
        return locator.findAll(contextType, parentContext);
    }
    
}
