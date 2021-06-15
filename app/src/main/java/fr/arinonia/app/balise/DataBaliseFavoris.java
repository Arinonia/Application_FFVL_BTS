package fr.arinonia.app.balise;

import java.util.ArrayList;
import java.util.List;

public class DataBaliseFavoris {
    private List<BaliseFavoris> balises = new ArrayList<BaliseFavoris>();

    public List<BaliseFavoris> getBalises() {
        return this.balises;
    }

    public void setBalises(List<BaliseFavoris> balises) {
        this.balises = balises;
    }
}
