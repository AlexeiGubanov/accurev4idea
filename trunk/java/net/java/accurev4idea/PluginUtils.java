/*
 * PluginUtils.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 11, 2005, 1:42:08 PM
 */
package net.java.accurev4idea;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: PluginUtils.java,v 1.1 2005/07/12 00:44:54 ifedulov Exp $
 * @since 0.1
 */
public class PluginUtils {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(PluginUtils.class);

    public static List getProjectModuleContentRoots(Project project) {
        List list = new LinkedList();
        ModuleManager manager = ModuleManager.getInstance(project);
        Module modules[] = manager.getModules();
        for (int i = 0; i < modules.length; i++) {
            Module module = modules[i];
            VirtualFile[] contentRoots = ModuleRootManager.getInstance(module).getContentRoots();
            for (int j = 0; j < contentRoots.length; j++) {
                VirtualFile file = contentRoots[j];
                if (log.isDebugEnabled()) {
                    log.debug("Adding content root ["+file.getPresentableUrl()+"] for ["+module.getName()+"] module.");
                }
                list.add(file);
            }
        }
        return list;
    }
}
