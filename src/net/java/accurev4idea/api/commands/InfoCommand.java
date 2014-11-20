/*
 * InfoCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 29, 2005, 1:37:03 PM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.AccuRevArguments;

/**
 * This implementation of {@link Command} is a proxy to "accurev info" call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class InfoCommand extends BaseAccurevCommand implements Command {
    /**
     * Initialized once because this set of arguments never change for this command;
     * contains 1 argument "info"
     */
    private static final Argument[] args = new Argument[] {
        AccuRevArguments.INFO
    };

    /**
     * Returns the Accurev arguments for "info" command
     *
     * @return  The args.
     */
    public Argument[] getArguments() {
        return args;
    }
}
