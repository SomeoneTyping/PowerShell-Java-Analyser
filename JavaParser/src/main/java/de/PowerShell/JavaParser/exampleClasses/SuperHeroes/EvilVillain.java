package de.PowerShell.JavaParser.exampleClasses.SuperHeroes;

public enum EvilVillain {

    IRON_MONGER("Iron Monger"),
    SANDMAN("Sandman"),
    SAURON("Sauron"),
    VOLDEMORT("Voldemort");

    private final String value;

    EvilVillain(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EvilVillain fromValue(String value) {
        for (EvilVillain e: EvilVillain.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
