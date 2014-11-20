/*
 * RevisionHistoryCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 6:18:38 PM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.AccuRevArguments;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This {@link Command} implementation corresponds to <tt>accurev stat -fx 'filepath'</tt> call.
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class StatusCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.STAT);
        add(AccuRevArguments.FX);
    }};

    public StatusCommand(File file) {
        args.add(new Argument(new File(file.getAbsolutePath()).getName()));
    }

    public StatusCommand(StatusSelectionType status) {
        if(status == null) {
            throw new IllegalArgumentException("StatusSelectionType can't be null.");
        }
        args.add(new Argument(status.getName()));
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
