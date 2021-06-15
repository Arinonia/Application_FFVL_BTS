package fr.arinonia.app.balise;

import java.util.List;


public class Balise {


    private final String nom;
    private final String etat;
    private final String pression_atmospherique;
    private final String temperature;
    private final String hygrometrie;
    private final String vitesse_vent;
    private final String direction_vent;
    private final String vent_minimum;
    private final String vent_maximum;
    private final List<WindSpeed> wind_history;

    /**
     *
     * @param nom : String
     * @param etat : String
     * @param pression_atmospherique : String
     * @param temperature : String
     * @param hygrometrie : String
     * @param vitesse_vent : String
     * @param direction_vent : String
     * @param vent_minimum : String
     * @param vent_maximum : String
     * @param wind_history : String
     */
    public Balise(String nom, String etat, String pression_atmospherique, String temperature, String hygrometrie, String vitesse_vent, String direction_vent, String vent_minimum, String vent_maximum, List<WindSpeed> wind_history) {
        this.nom = nom;
        this.etat = etat;
        this.pression_atmospherique = pression_atmospherique;
        this.temperature = temperature;
        this.hygrometrie = hygrometrie;
        this.vitesse_vent = vitesse_vent;
        this.direction_vent = direction_vent;
        this.vent_minimum = vent_minimum;
        this.vent_maximum = vent_maximum;
        this.wind_history = wind_history;
    }

    public String getNom() {
        return nom;
    }

    public String getEtat() {
        return etat;
    }

    public String getPression_atmospherique() {
        return pression_atmospherique;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHygrometrie() {
        return hygrometrie;
    }

    public String getVitesse_vent() {
        return vitesse_vent;
    }

    public String getDirection_vent() {
        return direction_vent;
    }

    public String getVent_minimum() {
        return vent_minimum;
    }

    public String getVent_maximum() {
        return vent_maximum;
    }

    public List<WindSpeed> getWind_history() {
        return wind_history;
    }
}
