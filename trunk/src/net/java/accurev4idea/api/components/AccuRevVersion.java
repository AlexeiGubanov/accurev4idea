/*
 * AccuRevVersion.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 4:31:40 PM
 */
package net.java.accurev4idea.api.components;

/**
 * POJO describing a "version" of a given "file" in AccuRev subsystem.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class AccuRevVersion {
    private CompositeVersion virtual;
    private CompositeVersion real;
    private CompositeVersion named;
    private CompositeVersion ancestor;

    public CompositeVersion getVirtual() {
        return virtual;
    }

    public void setVirtual(CompositeVersion virtual) {
        this.virtual = virtual;
    }

    public CompositeVersion getReal() {
        return real;
    }

    public void setReal(CompositeVersion real) {
        this.real = real;
    }

    public CompositeVersion getNamed() {
        return named;
    }

    public void setNamed(CompositeVersion named) {
        this.named = named;
    }

    public CompositeVersion getAncestor() {
        return ancestor;
    }

    public void setAncestor(CompositeVersion ancestor) {
        this.ancestor = ancestor;
    }

    public String toString() {
        return "AccuRevVersion{" +
               "virtual='" + virtual + "'" +
               ", real='" + real + "'" +
               ", named='" + named + "'" +
               ", ancestor='" + ancestor + "'" +
               "}";
    }
}
