package com.redhat.darcy.ui;

import com.redhat.darcy.DarcyException;

public class NotFoundException extends DarcyException {
    private static final long serialVersionUID = 3099804576753238242L;
    
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(Throwable cause) {
        super(cause);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NotFoundException(Class<?> type, Locator locator) {
        this("Could not find: " + type + ", by locator: " + locator);
    }
    
    public NotFoundException(Class<?> type, Locator locator, Throwable cause) {
        this("Could not find: " + type + ", by locator: " + locator, cause);
    }
}
