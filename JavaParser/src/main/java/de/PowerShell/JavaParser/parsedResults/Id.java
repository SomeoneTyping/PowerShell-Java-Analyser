package de.PowerShell.JavaParser.parsedResults;

public class Id {

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
