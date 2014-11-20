/*
 * AccuRevDiffContent.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 14, 2005, 7:58:14 PM
 */
package net.java.accurev4idea.components;

import com.intellij.openapi.diff.DiffContent;


/**
 * POJO Encapsulating data needed to display a single panel in two panel diff pane.
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: DiffPanelDataElement.java,v 1.1 2005/06/19 03:35:15 ifedulov Exp $
 * @since 0.1
 */
public class DiffPanelDataElement {
    private String title;
    private DiffContent diffContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DiffContent getDiffContent() {
        return diffContent;
    }

    public void setDiffContent(DiffContent diffContent) {
        this.diffContent = diffContent;
    }

}
