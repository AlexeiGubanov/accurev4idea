/*
 * Copyright (c) 2003-2005, Inversoft, All Rights Reserved
 *
 * This software is distribuable under the GNU Lesser General Public License.
 * For more information visit gnu.org.
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.Newline;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.components.Locking;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class is a command for Accurev that creates a workspace.
 * This also verifies that the workspace doesn't already exist.
 * </p>
 * 
 * @author  Brian Pontarelli
 */
public class MakeWorkspaceCommand extends BaseAccurevCommand implements Command {
    private static final Logger log = Logger.getLogger(MakeWorkspaceCommand.class);

    private String name;
    private String stream;
    private File location;
    private Newline newline;
    private Locking locking;
    private String reference;

    /**
     * Constructs a <code>MakeWorkspaceCommand</code> that when executed will create an Accurev
     * workspace.
     *
     * @param   stream The Stream to associate the workspace with.
     * @param   location The location of the workspace on the local file system.
     */
    public MakeWorkspaceCommand(String stream, File location) {
        this(stream, location, null, null, null, null);
    }

    /**
     * Constructs a <code>MakeWorkspaceCommand</code> that when executed will create an Accurev
     * workspace.
     *
     * @param   stream The Stream to associate the workspace with.
     * @param   location The location of the workspace on the local file system.
     * @param   name (Optional defaults to stream name) The name of the workspace to create.
     * @param   newline (Optional defaults to OS define separator) The newline separator object that
     *          is used for all files in the workspace.
     * @param   locking (Optional defaults to none) The locking schema for the workspace.
     * @param   reference (Optional) A reference that was created elsewhere on the file system, unix
     *          only.
     */
    public MakeWorkspaceCommand(String stream, File location, String name, Newline newline,
                                Locking locking, String reference) {
        if (stream == null || location == null) {
            throw new IllegalArgumentException("stream and location are required for the " +
                "MakeWorkspaceCommand");
        }

        if (locking != null && reference != null) {
            throw new IllegalArgumentException("Either a locking type or a reference may be used, " +
                "but not both.");
        }

        this.stream = stream;
        this.location = location;
        this.newline = newline;
        this.locking = locking;
        this.reference = reference;

        if (name == null) {
            this.name = stream;
        } else {
            this.name = name;
        }
    }

    /**
     * Returns the Accurev arguments.
     *
     * @return  The args.
     */
    public Argument[] getArguments() {
        List args = new ArrayList();
        args.add(new Argument("mkws"));
        args.add(new Argument("-w", this.name));
        args.add(new Argument("-b", this.stream));
        try {
            args.add(new Argument("-l", this.location.getCanonicalPath()));
        } catch (IOException ioe) {
            log.debug("Unable to resolve the canonical path of the supplied file...", ioe);
            args.add(new Argument("-l", this.location.getAbsolutePath()));
        }

        if (this.newline != null) {
            args.add(new Argument("-e", super.determineOption(this.newline)));
        }

        if (this.locking != null) {
            args.add(new Argument("-k", super.determineOption(this.locking)));
        }

        if (this.reference != null) {
            args.add(new Argument("-k", "l"));
            args.add(new Argument("-r", this.reference));
        }

        return (Argument[]) args.toArray(new Argument[args.size()]);
    }
}