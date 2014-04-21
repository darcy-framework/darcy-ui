package com.redhat.darcy;

public class DarcyException extends RuntimeException {
    private static final long serialVersionUID = 2402576231289911388L;
    
    public DarcyException() {
        super();
    }
    
    public DarcyException(String message) {
        super(message);
    }
    
    public DarcyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DarcyException(Throwable cause) {
        super(cause);
    }
}
