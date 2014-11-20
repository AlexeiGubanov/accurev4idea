/*
 * RevisionHistoryCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 6:18:38 PM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.AccuRevArguments;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.exec.Argument;

import java.util.LinkedList;
import java.util.List;
import java.io.File;

/**
 * This {@link Command} implementation corresponds to <tt>accurev hist -fx 'filepath'</tt> call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class RevisionHistoryCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.HIST);
        add(AccuRevArguments.FX);
    }};

    public RevisionHistoryCommand(AccuRevFile file) {
        args.add(new Argument(new File(file.getAbsolutePath()).getName()));
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
