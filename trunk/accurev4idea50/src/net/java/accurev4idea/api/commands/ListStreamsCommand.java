/*
 * ListStreamsCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 28, 2005, 7:59:49 AM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.AccuRevArguments;

import java.util.ArrayList;
import java.util.List;

/**
 * This commands handles the "show streams" command for accurev.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class ListStreamsCommand extends BaseAccurevCommand implements Command {
    /**
     * Arguments that never change for any of the constructors in this class
     */
    private final List args = new ArrayList() {{
        add(AccuRevArguments.SHOW);
        add(AccuRevArguments.FX);
    }};

    /**
     * Construct command to be executed with depot name (show -p "depot name" streams)
     * @param depotName
     */
    public ListStreamsCommand(String depotName) {
        args.add(new Argument("-p", depotName));
        args.add(AccuRevArguments.STREAMS);
    }

    /**
     * Construct command to be executed with depot name and stream name (show -p "depot name" -s "stream name" streams)
     * @param depotName
     * @param streamName
     */
    public ListStreamsCommand(String depotName, String streamName) {
        args.add(new Argument("-p", depotName));
        args.add(new Argument("-s", streamName));
        args.add(AccuRevArguments.STREAMS);
    }

    /**
     * @see Command#getArguments()
     */ 
    public Argument[] getArguments() {
        return (Argument[]) args.toArray(new Argument[args.size()]);
    }
}
