package de.PowerShell.JavaParser.exampleClasses.SuperHeroes;

import de.PowerShell.JavaParser.exampleClasses.Citizens.TonyStark;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IronMan extends TonyStark implements ISuperHero {

    private int rescuedCitizens;
    private int countOfDiscussionsWithCaptainAmerica;
    private static EvilVillain villain = EvilVillain.IRON_MONGER;

    @Override
    public void flyTo(Destination destination) {

    }

    @Override
    public void applySuperStrength(EvilVillain villain) {

    }

    @Override
    public EvilVillain whoIsYourRival() {
        return villain;
    }
}
