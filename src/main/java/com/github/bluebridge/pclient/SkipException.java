package com.github.bluebridge.pclient;

/**
 * Thrown by conversion method if argument cannot be
 * translated to result to be returned.
 *
 * Daneel Yaitskov
 */
public class SkipException extends Exception {
    public SkipException() {
    }

    public SkipException(String s) {
        super(s);
    }

    public SkipException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
