/*
 * AccuRevRuntimeException.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on June 24, 2005
 */
package net.java.accurev4idea.api.exceptions;

/**
 * This exception is thrown when there is an unexpected condition during
 * parsing of a response received from AccuRev server.
 *
 * @author <a href="mailto:igor@fedulov.com">Igor Fedulov</a>
 * @version 1.1
 * @since 1.1
 */
public class ParseException extends RuntimeException {
    /**
     * @see RuntimeException()
     */
    public ParseException() {
        super();
    }

    /**
     * @see RuntimeException(String)
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException(Throwable)
     */
    public ParseException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException(String, Throwable)
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
