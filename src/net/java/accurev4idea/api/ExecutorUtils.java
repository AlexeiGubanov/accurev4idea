/*
 * ExecutorUtils.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:39:53 PM
 */
package net.java.accurev4idea.api;

import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.exec.Executor;
import net.java.accurev4idea.api.exec.CommandResult;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.api.exceptions.ExecutionException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;

/**
 * This utility class provides access to methods from {@link Executor} class and
 * removes the need to catch {@link ExecutionException} as well as provides
 * access to the {@link CommandResult} if exception is occured by saving a copy in
 * instance of {@link AccuRevRuntimeException} that is being thrown instead.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @since 1.0
 */
public class ExecutorUtils {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ExecutorUtils.class);

    /**
     * Execute {@link Command} using {@link Executor} instance.
     *
     * @param command
     * @return
     */
    public static CommandResult executeAccuRevCommand(Command command) {
        return executeAccuRevCommand(command, null);
    }

    /**
     * Execute {@link Command} using {@link Executor} instance.
     *
     * @param command
     * @param parentDir
     * @return
     */
    public static CommandResult executeAccuRevCommand(Command command, File parentDir) {
        return executeAccuRevCommand(command, parentDir, Collections.EMPTY_LIST);
    }

    /**
     * Execute {@link Command} using {@link Executor} instance.
     *  If the list of {@link CommandExecListener} is not null, they will be
     *  fired with the {@link CommandResult} object provided as an argument.
     *
     * @param command
     * @param parentDir
     * @param listeners - Expects a List of CommandExecListener objects
     * @return
     */
    public static CommandResult executeAccuRevCommand(Command command, File parentDir, List listeners) {
        CommandResult commandResult = null;
        Executor executor = new Executor();
        try {
            commandResult = executor.execute(command, parentDir);
            fireListeners(commandResult, listeners);
        } catch (ExecutionException e) {
            fireListeners(e.getCommandResult(), listeners);
            log.error(e.getLocalizedMessage(), e);
            throw new AccuRevRuntimeException(e.getLocalizedMessage(), e);
        }
        // if debuging is enabled, output command result contents
        log.debug(commandResult);
        // because the "isSuccess()" can be false and executor might throw an exception as well this
        // give two possible ways to fail an execution, therefore not to modify the api, instead check the command
        // result before returning and if it's a failure throw the SavantExecutionException with commandResult.toString()
        // as the message
        // NOTE: Because accurev stat -fx <file_that_does_not_exist> is BROKEN, the result
        //       is returned as "0" when there was a error!
        if (commandResult.isSuccess() && StringUtils.isBlank(commandResult.getErr())) {
            return commandResult;
        } else {
            log.error(commandResult);
            throw new AccuRevRuntimeException(commandResult);
        }
    }

    private static void fireListeners(CommandResult result, List listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            for (Iterator iter = listeners.iterator(); iter.hasNext();) {
                CommandExecListener listener = (CommandExecListener) iter.next();
                listener.commandExecuted(result);
            }
        }
    }
}
