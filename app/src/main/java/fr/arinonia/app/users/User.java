package fr.arinonia.app.users;

import java.util.ArrayList;
import java.util.List;

import fr.arinonia.app.balise.Balise;

public class User {

    private List<Balise> balises = new ArrayList<>();

    public User(List<Balise> balises) {
        this.balises = balises;
    }


    public List<Balise> getBalises() {
        return balises;
    }

    public void setBalises(List<Balise> balises) {
        this.balises = balises;
    }
}