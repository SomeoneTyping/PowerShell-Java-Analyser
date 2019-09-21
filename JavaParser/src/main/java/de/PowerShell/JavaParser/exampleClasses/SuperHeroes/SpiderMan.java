package de.PowerShell.JavaParser.exampleClasses.SuperHeroes;

public class SpiderMan implements ISuperHero {

    private SpidermanDetails details = new SpidermanDetails();

    public SpiderMan() {
        details.kilometersFromHome = 10000;
        details.dressIsInCleansing = true;
    }

    @Override
    public void flyTo(Destination destination) {

        if (!details.dressIsInCleansing) {
            details.kilometersFromHome += 10;
        }
    }

    @Override
    public void applySuperStrength(EvilVillain villain) {

    }

    @Override
    public EvilVillain whoIsYourRival() {
        return null;
    }

    private class SpidermanDetails {
        boolean dressIsInCleansing;
        int kilometersFromHome;
    }

}
