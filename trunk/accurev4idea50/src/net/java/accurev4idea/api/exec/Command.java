/*
 * Copyright (c) 2003-2004, Inversoft, All Rights Reserved
 *
 * This software is distribuable under the GNU Lesser General Public License.
 * For more information visit gnu.org.
 */
package net.java.accurev4idea.api.exec;

/**
 * <p>
 * This interface defines an executable command (CLI) that is run
 * by the Savant {@link Executor}.
 * </p>
 * 
 * @author  Brian Pontarelli
 */
public interface Command {
    /**
     * Returns the name of the command to execute.
     *
     * @return  The name of the command to execute.
     */
    String getCommand();

    /**
     * Returns an array of command line arguments, if any, that the command requires for execution.
     *
     * @return  An array or null if no arguments are needed.
     */
    Argument[] getArguments();
}