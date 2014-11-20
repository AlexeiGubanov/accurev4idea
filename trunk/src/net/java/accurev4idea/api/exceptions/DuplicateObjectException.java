/*
 * DuplicateObjectException.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on June 24, 2005
 */
package net.java.accurev4idea.api.exceptions;

/**
 * This exception is thrown when there is a duplicate object found during
 * accurev operation. This is thrown usually on attempt to create a new
 * element in AccuRev subsystem.
 *
 * @author <a href="mailto:igor@fedulov.com">Igor Fedulov</a>
 * @version $Id: DuplicateObjectException.java,v 1.1 2005/11/05 16:56:10 ifedulov Exp $
 * @since 1.0
 */
public class DuplicateObjectException extends RuntimeException {
    /**
     * @see RuntimeException()
     */
    public DuplicateObjectException() {
        super();
    }

    /**
     * @see RuntimeException(String)
     */
    public DuplicateObjectException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException(Throwable)
     */
    public DuplicateObjectException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException(String, Throwable)
     */
    public DuplicateObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
