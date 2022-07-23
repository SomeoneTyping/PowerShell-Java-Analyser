package de.PowerShell.JavaParser.parsedResults;

import java.io.Serializable;

public class Id implements Serializable {

    private String id;

    private Id(String id) {
        this.id = id;
    }

    public static Id valueOf(String id) {
        return new Id(id);
    }

    public String getIdAsString() {
        return id;
    }
}
