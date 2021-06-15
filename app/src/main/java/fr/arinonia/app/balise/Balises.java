package fr.arinonia.app.balise;

public class Balises {

    private final String idBalise;
    private final String latitude;
    private final String longitude;
    private final String etat;

    /**
     *
     * @param idBalise : String
     * @param latitude : String
     * @param longitude : String
     * @param etat : String
     */
    public Balises(String idBalise, String latitude, String longitude, String etat) {
        this.idBalise = idBalise;
        this.latitude = latitude;
        this.longitude = longitude;
        this.etat = etat;
    }

    public String getIdBalise() {
        return idBalise;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getEtat() {
        return etat;
    }
}
