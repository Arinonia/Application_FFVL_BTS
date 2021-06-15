package fr.arinonia.app.users;

public class UserData {

    private final String id;
    private final String prenom;
    private final String nom;
    private final String email;
    private final String password;

    public UserData(String id, String prenom, String nom, String email, String password) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
