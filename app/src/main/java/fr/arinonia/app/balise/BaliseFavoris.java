package fr.arinonia.app.balise;

public class BaliseFavoris {

    private final String idBalise;
    private final String nom;
    private final String latitude;
    private final String longitude;

    /**
     *
     * @param idBalise : String
     * @param nom : String
     * @param latitude : String
     * @param longitude : String
     */
    public BaliseFavoris(String idBalise, String nom, String latitude, String longitude) {
        this.idBalise = idBalise;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIdBalise() {
        return idBalise;
    }

    public String getNom() {
        return nom;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
