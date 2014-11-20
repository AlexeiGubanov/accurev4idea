/*
 * Copyright (c) 2003-2005, Inversoft, All Rights Reserved
 *
 * This software is distribuable under the GNU Lesser General Public License.
 * For more information visit gnu.org.
 */
package net.java.accurev4idea.api.exceptions;

import net.java.accurev4idea.api.exec.CommandResult;

/**
 * <p>
 * This exception is thrown when the Savant {@link net.java.accurev4idea.api.exec.Executor} fails
 * during command execution.
 * </p>
 *
 * @author  Brian Pontarelli
 */
public class ExecutionException extends Exception {
    /**
     * Command result this execution exception is based on, initialzied to
     * null-safe version {@link net.java.accurev4idea.api.exec.CommandResult#EMPTY_COMMAND_RESULT}
     */
    private CommandResult commandResult = CommandResult.EMPTY_COMMAND_RESULT;

    public ExecutionException() {
        super();
    }

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Allow for constructor that accepts CommandResult as an argument
     * for better manipulation of error message source (i.e. some times
     * error message might be present in output stream instead of error
     * stream so creating exception with just "commandResult.getError()" is
     * not sufficient.
     *
     * @param commandResult the {@link CommandResult} instance for failed execution
     */
    public ExecutionException(CommandResult commandResult) {
        super(commandResult.toString());
        this.commandResult = commandResult;
    }

    /**
     * Return {@link #commandResult} field value. This method will return null
     * if exception was created using different than {@link net.java.accurev4idea.api.exceptions.SavantExecutionException(CommandResult)}
     * constructor.
     *
     * @return {@link CommandResult} from {@link #commandResult} field, or null
     */
    public CommandResult getCommandResult() {
        return commandResult;
    }
}