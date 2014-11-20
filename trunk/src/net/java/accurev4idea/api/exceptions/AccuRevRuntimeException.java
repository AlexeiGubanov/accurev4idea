/*
 * AccuRevRuntimeException.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on June 24, 2005
 */
package net.java.accurev4idea.api.exceptions;

import net.java.accurev4idea.api.exec.CommandResult;

/**
 * This exception is thrown when during execution of accurev command process
 * returned a error or threw an Exception. In former case copy of {@link CommandResult}
 * object will be saved in this exception.
 *
 * @author <a href="mailto:igor@fedulov.com">Igor Fedulov</a>
 * @version $Id: AccuRevRuntimeException.java,v 1.1 2005/11/05 16:56:10 ifedulov Exp $
 * @since 1.0
 */
public class AccuRevRuntimeException extends RuntimeException {
    /**
     * Command result this execution exception is based on, initialzied to
     * null-safe version {@link net.java.accurev4idea.api.exec.CommandResult#EMPTY_COMMAND_RESULT}
     */
    private CommandResult commandResult = CommandResult.EMPTY_COMMAND_RESULT;

    /**
     * @see RuntimeException()
     */
    public AccuRevRuntimeException() {
        super();
    }

    /**
     * @see RuntimeException(String)
     */
    public AccuRevRuntimeException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException(Throwable)
     */
    public AccuRevRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * @see RuntimeException(String, Throwable)
     */
    public AccuRevRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see net.java.accurev4idea.api.exceptions.AccuRevRuntimeException(String, CommandResult)
     */
    public AccuRevRuntimeException(CommandResult commandResult) {
        this(commandResult.toString(), commandResult);
    }

    /**
     * Allow for constructor that accepts custom message and {@link CommandResult}
     * as an argument for better manipulation of error message source (i.e. some times
     * error message might be present in output stream instead of error
     * stream so creating exception with just "commandResult.getError()" is
     * not sufficient.
     *
     * @param message custom message to use
     * @param commandResult the {@link CommandResult} instance for failed execution
     */
    public AccuRevRuntimeException(String message, CommandResult commandResult) {
        super(message);
        this.commandResult = commandResult;
    }

    /**
     * Return {@link #commandResult} field value. This method will return {@link CommandResult#EMPTY_COMMAND_RESULT}
     * if exception was created using different than {@link net.java.accurev4idea.api.exceptions.AccuRevRuntimeException(CommandResult)}
     * constructor.
     *
     * @return {@link CommandResult} from {@link #commandResult} field, or {@link CommandResult#EMPTY_COMMAND_RESULT}
     */
    public CommandResult getCommandResult() {
        return commandResult!=null? commandResult: CommandResult.EMPTY_COMMAND_RESULT;
    }
}
