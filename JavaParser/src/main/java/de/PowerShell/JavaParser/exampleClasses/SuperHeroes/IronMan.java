package de.PowerShell.JavaParser.exampleClasses.SuperHeroes;

import java.util.List;

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
    private Integer electricityConsumptionInMW = 220;
    private List<String> quotes;
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

    public int getRescuedCitizens() {
        return rescuedCitizens;
    }

    public void setRescuedCitizens(int rescuedCitizens) {
        this.rescuedCitizens = rescuedCitizens;
    }

    public int getCountOfDiscussionsWithCaptainAmerica() {
        return countOfDiscussionsWithCaptainAmerica;
    }

    public void setCountOfDiscussionsWithCaptainAmerica(int countOfDiscussionsWithCaptainAmerica) {
        this.countOfDiscussionsWithCaptainAmerica = countOfDiscussionsWithCaptainAmerica;
    }

    public Integer getElectricityConsumptionInMW() {
        return electricityConsumptionInMW;
    }

    public void setElectricityConsumptionInMW(Integer electricityConsumptionInMW) {
        this.electricityConsumptionInMW = electricityConsumptionInMW;
    }

    public List<String> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<String> quotes) {
        this.quotes = quotes;
    }

    public static EvilVillain getVillain() {
        return villain;
    }

    public static void setVillain(EvilVillain villain) {
        IronMan.villain = villain;
    }

}
