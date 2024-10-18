package com.griefcraft.scripting;

public class ModuleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ModuleException() {
        super();
    }

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(Throwable cause) {
        super(cause);
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }

}