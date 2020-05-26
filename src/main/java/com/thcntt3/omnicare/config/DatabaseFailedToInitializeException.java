package com.thcntt3.omnicare.config;

import java.io.IOException;

public class DatabaseFailedToInitializeException extends Exception {
    public DatabaseFailedToInitializeException() {
        super("Cannot initialize Firebase.", new IOException("Cannot find credentials."));
    }
}
