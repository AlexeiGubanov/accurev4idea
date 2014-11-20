package net.java.accurev4idea.api;

import net.java.accurev4idea.api.exec.CommandResult;

/**
 * $Id: CommandExecListener.java,v 1.1 2005/11/05 16:56:14 ifedulov Exp $
 * User: aantonov
 * Date: Nov 3, 2005
 * Time: 9:47:30 AM
 */
public interface CommandExecListener {

    public void commandExecuted(CommandResult result);
}
