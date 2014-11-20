/*
 * AccuRevDAOImpl.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 28, 2005, 5:55:42 PM
 */
package net.java.accurev4idea.api;

import net.java.accurev4idea.api.commands.*;
import net.java.accurev4idea.api.components.*;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.api.exceptions.NoSuchObjectException;
import net.java.accurev4idea.api.exceptions.UpdateException;
import net.java.accurev4idea.api.exec.CommandResult;
import net.java.accurev4idea.api.parsers.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * CLI based AccuRev data access layer, provides facade for running accurev binary on command line and
 * getting the results extracted and converted into understandable POJOs.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRev.java,v 1.13 2006/02/08 22:40:29 ifedulov Exp $
 * @since 1.0
 */
public final class AccuRev {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRev.class);

    private static Map workspacesCache = new HashMap();

    /**
     */
    public static AccuRevInfo getInfo() {
        return AccuRevInfoParser.extractAccuRevInfo(ExecutorUtils.executeAccuRevCommand(new InfoCommand()).getOut());
    }

    /**
     */
    public static List getDepots() {
        return DepotParser.extractDepots(ExecutorUtils.executeAccuRevCommand(new ListDepotsCommand()).getOut());
    }

    /**
     */
    public static Map getWorkspacesForCurrentPrincipal(final List listeners) {
        if(workspacesCache.isEmpty()) {
            synchronized (workspacesCache) {
                // use double checking to avoid any second caching attempts. This might happen because
                // first check for is empty is not synchronized
                if(workspacesCache.isEmpty()) {
                    workspacesCache = Collections.unmodifiableMap(WorkspaceParser.extractWorkspaces(ExecutorUtils.executeAccuRevCommand(new ListWorkspacesCommand(), null, listeners).getOut()));
                }
            }
        }
        return workspacesCache;
    }

    /**
     */
    public static Workspace getWorkspaceByNameForCurrentPrincipal(String name, List listeners) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Workspace name [" + name + "] can't be null or empty string.");
        }
        Map workspaces = getWorkspacesForCurrentPrincipal(listeners);
        AccuRevInfo info = getInfo();
        // if name of the workspace that is being looked for doesn't end with name
        // of the current principal, append the name of the principal to the name of the
        // workspace because AccuRev does this automatically internally and therefore
        // all keys in returned map of all workspaces for current principal will have
        // name of the principal appended
        if (!name.endsWith(info.getPrincipal())) {
            name += "_" + info.getPrincipal();
        }
        // lookup workspace by name in the returned map
        Workspace workspace = (Workspace) workspaces.get(name);
        if (workspace == null) {
            throw new NoSuchObjectException("Unable to find Workspace by given [" + name + "] name in Map of [" + ToStringBuilder.reflectionToString(workspaces.values(), ToStringStyle.MULTI_LINE_STYLE) + "] workspaces.");
        } else {
            return workspace;
        }
    }

    /**
     */
    public static Stream getStreamsTree(Depot depot) {
        return StreamParser.extractStreamsTree(ExecutorUtils.executeAccuRevCommand(new ListStreamsCommand(depot.getName())).getOut());
    }

    /**
     */
    public static String getBackedFileContents(File file, List listeners) {
        if (log.isDebugEnabled()) {
            log.debug("Getting backed file contents for [" + file.getAbsolutePath() + "]");
        }
        validateFile(file);
        AccuRevFile accuRevFile = getAccuRevFile(file, listeners);
        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new CatFileCommand(accuRevFile), file.getParentFile());
        return commandResult.getOut();
    }

    /**
     */
    public static String getLastKeptFileContents(File file, List commandExecListeners) {
        if(log.isDebugEnabled()) {
            log.debug("Getting last kept file contents for ["+ file.getAbsolutePath()+"]");
        }
        validateFile(file);
        AccuRevFile accuRevFile = getAccuRevFile(file, commandExecListeners);

        return getFileContentsByVersion(file, accuRevFile.getVersion(), commandExecListeners);
    }

    /**
     */
    public static String getFileContentsByVersion(File file, AccuRevVersion version, List commandExecListeners) {
        validateFile(file);
        if (!validateVersion(version)) {
            return "";
        }

        // This is being done to combat accurev's reluctance to handle defunct files correctly.
        AccuRevFile aFile = getAccuRevFile(file, commandExecListeners);

        File parentDir = file.getParentFile();
        try {
            parentDir = file.getCanonicalFile().getParentFile();
        } catch (IOException ioe) {
            throw new AccuRevRuntimeException("This is a bug in AccuRev cat command where it does not resolve the sym-link correctly", ioe);
        }

        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new CatFileCommand(aFile, version), parentDir, commandExecListeners);
        return commandResult.getOut();
    }

    /**
     */
    public static List getRevisionHistory(final AccuRevFile accuRevFile, final File file, final List listeners) {
        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new RevisionHistoryCommand(accuRevFile), file.getParentFile(), listeners);
        return RevisionHistoryParser.extractRevisionHistory(commandResult.getOut(), accuRevFile);
    }

    /**
     */
    public static AccuRevFile getAccuRevFile(final File file, final List listeners) {
        validateFile(file);
        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new StatusCommand(file), file.getParentFile(), listeners);
        final Workspace workspaceByFileForCurrentPrincipal = getWorkspaceByFileForCurrentPrincipal(file, listeners);
        return AccuRevFileParser.extractAccuRevFile(commandResult.getOut(), new File(workspaceByFileForCurrentPrincipal.getLocation()));
    }

    /**
     */
    public static AccuRevFileStatus getAccuRevFileStatus(File file, final List listeners) {
        return getAccuRevFile(file, listeners).getStatus();
    }

    /**
     */
    public static Workspace createWorkspace(Stream parentStream, File location, String name, Newline newline, Locking locking, List listeners) {
        // run validation
        validateParametersForCreateWorkspaceCall(parentStream, location, name, newline, locking);

        // execute make workspace command to create a workspace
        ExecutorUtils.executeAccuRevCommand(new MakeWorkspaceCommand(parentStream.getName(), location, name, newline, locking, null));

        // extract workspace information out from the server
        Workspace workspace = getWorkspaceByNameForCurrentPrincipal(name, listeners);

        // set the parent stream reference
        workspace.setParent(parentStream);

        // execute update workspace command to populate workspace with files from backing stream
        updateWorkspace(workspace.getName(), listeners);

        // processing is done, return result
        return workspace;
    }

    /**
     */
    public static void updateWorkspace(String name, List listeners) {
        // validate the name, it can't be null or blank
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Workspace name can't be null or empty string.");
        }
        // execute update workspace command to populate workspace with files from backing stream
        try {
            ExecutorUtils.executeAccuRevCommand(new UpdateCommand(false, name, null));
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // check the out response of the error
            CommandResult cr = e.getCommandResult();
            String message = cr.getErr();
            log.debug(message);
            // error message will contain something like this:
            // ...
            // Some of the elements in your workspace have been modified
            // and are not in your default group. Use 'accurev stat -n' to
            // get a list of these files. Then resolve all modifications
            // with anchor, keep, or purge before trying update again.
            // ...
            // NOTE: Following is a very lame condition checking
            if (message.indexOf("accurev stat -n") > -1) {
                // get workspace reference
                Workspace workspace = (Workspace) getWorkspacesForCurrentPrincipal(listeners).get(name);
                log.assertLog(workspace != null, "Workspace can't be found by name [" + name + "]");
                // extract list of files using stat -n command
                List files = getAccuRevFilesInFolderByStatus(workspace.getLocation(), StatusSelectionType.SELECT_MODIFIED_NON_MEMBER, listeners);
                throw new UpdateException("Some of the elements in your workspace [" + name + "] have been modified and are not in your default group", files);
            }
        }
    }

    /**
     */
    public static CommandResult updateDirectory(File location, List listeners) {
        validateFile(location);
        // execute update workspace command to populate workspace with files from backing stream
        try {
            return ExecutorUtils.executeAccuRevCommand(new UpdateCommand(), location, listeners);
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // check the out response of the error
            CommandResult cr = e.getCommandResult();
            String message = cr.getErr();
            log.debug(message);
            // error message will contain something like this:
            // ...
            // Some of the elements in your workspace have been modified
            // and are not in your default group. Use 'accurev stat -n' to
            // get a list of these files. Then resolve all modifications
            // with anchor, keep, or purge before trying update again.
            // ...
            // NOTE: Following is a very lame condition checking
            if (message.indexOf("accurev stat -n") > -1) {
                // extract list of files using stat -n command
                List files = getAccuRevFilesInFolderByStatus(location.getAbsolutePath(), StatusSelectionType.SELECT_MODIFIED_NON_MEMBER, listeners);
                throw new UpdateException("Some of the elements in directory [" + location.getAbsolutePath() + "] have been modified and are not in your default group", files);
            } else {
                throw e;
            }
        }
    }

    /**
     */
    public static Workspace getWorkspaceByFileForCurrentPrincipal(File file, final List listeners) {
        try {
            Map workspaces = getWorkspacesForCurrentPrincipal(listeners);
            for (Iterator i = workspaces.values().iterator(); i.hasNext();) {
                Workspace workspace = (Workspace) i.next();
                if (file.getCanonicalPath().startsWith(new File(workspace.getLocation()).getCanonicalPath())) {
                    return workspace;
                }
            }
            throw new NoSuchObjectException("Can't find workspace for given file [" + file.getCanonicalPath() + "]");
        } catch (IOException ioe) {
            log.error("Where the F..k is this file???", ioe);
            throw new NoSuchObjectException("Can't find workspace for given file [" + file.getAbsolutePath() + "]");
        }
    }

    /**
     */
    public static void revertToBacked(File file, List listeners) throws AccuRevRuntimeException, NoSuchObjectException {
        validateFile(file);
        ExecutorUtils.executeAccuRevCommand(new PurgeCommand(file), file.getParentFile(), listeners);
    }

    /**
     */
    public static void revertToLastKept(File file, List listeners) throws AccuRevRuntimeException, NoSuchObjectException {
        validateFile(file);
        ExecutorUtils.executeAccuRevCommand(new PopCommand(file), file.getParentFile(), listeners);
    }

    /**
     */
    public static List getAccuRevFilesInFolderByStatus(String location, StatusSelectionType type, List listeners) {
        // validate type
        if (type == null) {
            throw new IllegalArgumentException("Status selection type can't be null.");
        }
        if (log.isDebugEnabled()) {
            log.debug("Running StatusCommand with [" + type + "] selection type in [" + location + "]");
        }
        // get the workspace location into File
        final File workspaceDir = new File(location);
        // run status command with given "type" and in workspace directory
        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new StatusCommand(type), workspaceDir, listeners);
        // extract accurev files from command output
        return AccuRevFileParser.extractAccuRevFiles(commandResult.getOut(), workspaceDir);
    }

    public static List getAccuRevFilesInStream(Stream stream, String fileDir, List listeners) {
        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new FilesCommand(fileDir, stream), SystemUtils.getUserDir(), listeners);
        // extract accurev files from command output
        if (stream.isWorkspace()) {
            Workspace w = AccuRev.getWorkspaceByNameForCurrentPrincipal(stream.getName(), listeners);
            return AccuRevFileParser.extractAccuRevFiles(commandResult.getOut(), new File(w.getLocation()));
        } else {
            return AccuRevFileParser.extractAccuRevFiles(commandResult.getOut(), null);
        }
    }

    public static List keepFiles(String comment, List fileLocationsRelativeToWorkspaceRoot, File workspaceDir, List listeners) {
        CommandResult commandResult = ExecutorUtils.executeAccuRevCommand(new KeepCommand(comment, fileLocationsRelativeToWorkspaceRoot),
                workspaceDir, listeners);
        // extract accurev files from command output
        return AccuRevFileParser.extractAccuRevFiles(commandResult.getOut(), null);
    }

    public static void moveFile(File source, File destination,  List listeners) {
        Workspace workspace = getWorkspaceByFileForCurrentPrincipal(source, listeners);
        ExecutorUtils.executeAccuRevCommand(new MoveCommand(source, destination), new File(workspace.getLocation()), listeners);

    }

    public static void addFile(File file, List listeners) {
        Workspace workspace = getWorkspaceByFileForCurrentPrincipal(file.getParentFile(), listeners);
        ExecutorUtils.executeAccuRevCommand(new AddCommand(file), new File(workspace.getLocation()), listeners);
    }

    public static void deleteFile(File file, List listeners) {
        Workspace workspace = getWorkspaceByFileForCurrentPrincipal(file.getParentFile(), listeners);
        ExecutorUtils.executeAccuRevCommand(new DefunctCommand(file), new File(workspace.getLocation()), listeners);
    }


    private static void validateParametersForCreateWorkspaceCall(Stream parentStream, File location, String name, Newline newline, Locking locking) {
        // validate directory is not null
        if (location == null) {
            throw new IllegalArgumentException("Workspace directory location can't be null.");
        }
        // validate stream
        if (parentStream == null || parentStream.isWorkspace()) {
            throw new IllegalArgumentException("Parent stream [" + parentStream + "] is either null or is a workspace. Workspaces can't be nested.");
        }
        // ensure that name is specified
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Workspace name [" + name + "] can't be null or empty string.");
        }
        // validate new line and locking settings
        if (newline == null) {
            throw new IllegalArgumentException("Newline parameter can't be null, use Newline.DEFAULT instead.");
        }
        // validate locking
        if (locking == null) {
            throw new IllegalArgumentException("Locking parameter can't be null, use Locking.DEFAULT instead.");
        }
    }


    private static void validateFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File can't be null.");
        }
        // NOTE: DO NOT add a check for file existence. This command can cat the file even if it's not
        //       present on the local file system (the whole point)
    }

    private static boolean validateVersion(AccuRevVersion version) {
        if (version == null || version.getReal() == null) {
            return false;
        }
        return true;
    }

}