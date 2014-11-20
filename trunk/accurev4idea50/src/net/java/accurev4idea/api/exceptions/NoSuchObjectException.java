/*
 * NoSuchObjectException.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on June 24, 2005
 */
package net.java.accurev4idea.api.exceptions;

/**
 * This exception is thrown when a particular object wasn't found during
 * AccuRev operation. Usually this is thrown by methods that accept parameter
 * that is validated for presence.
 *
 * @author <a href="mailto:igor@fedulov.com">Igor Fedulov</a>
 * @version $Id: NoSuchObjectException.java,v 1.1 2005/11/05 16:56:10 ifedulov Exp $
 * @since 1.0
 */
public class NoSuchObjectException extends RuntimeException {
    /**
     * @see RuntimeException()
     */
    public NoSuchObjectException() {
        super();
    }

    /**
     * @see RuntimeException(String)
     */
    public NoSuchObjectException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException(Throwable)
     */
    public NoSuchObjectException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException(String, Throwable)
     */
    public NoSuchObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
