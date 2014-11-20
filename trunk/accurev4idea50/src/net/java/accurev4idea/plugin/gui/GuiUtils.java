package net.java.accurev4idea.plugin.gui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;

import javax.swing.ImageIcon;
import java.net.URL;

import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.api.components.AccuRevFile;

/**
 * $Id $
 * User: aantonov
 * Date: Jun 8, 2005
 * Time: 11:17:37 AM
 */
public class GuiUtils {
    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getInstance(GuiUtils.class.getName());

    public static final ImageIcon ACCUREV_ICON_16x16 = GuiUtils.createImageIcon("/fw/images/accli16.gif");
    /**
     * Create an {@link javax.swing.ImageIcon} for the given resource path. If icon can't be created from
     * given path <tt>null</tt> will be returned, otherwise instance of {@link javax.swing.ImageIcon(java.net.URL)} is
     * returned.
     *
     * @param path to look for icon
     * @return ImageIcon if resource is available, null otherwise
     */
    public static ImageIcon createImageIcon(String path) {
        URL imgURL = GuiUtils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            log.error("Couldn't find file [" + path + "]");
            return null;
        }
    }

    public static void showErrorMessage(AccuRevRuntimeException e) {
        showErrorMessage(e.getCommandResult().getErr());
    }

    public static void showErrorMessage(String message) {
        Messages.showErrorDialog(message, "AccuRev Plugin Error");
    }

    public static void showErrorMessage(RuntimeException e) {
        showErrorMessage(e.getLocalizedMessage());
    }

    public static String getFileName(AccuRevFile file) {
        String separator = System.getProperty("file.separator");
        int indexOfLastSlash = file.getAbsolutePath().lastIndexOf(separator);
        return file.getAbsolutePath().substring(indexOfLastSlash+1);
    }
}
