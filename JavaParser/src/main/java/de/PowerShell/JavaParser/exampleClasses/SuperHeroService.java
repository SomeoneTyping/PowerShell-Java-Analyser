package de.PowerShell.JavaParser.exampleClasses;

import de.PowerShell.JavaParser.exampleClasses.SuperHeroes.ISuperHero;
import de.PowerShell.JavaParser.exampleClasses.SuperHeroes.IronMan;

public class SuperHeroService {

    public ISuperHero callForHelp() {
        return new IronMan();
    }
}
