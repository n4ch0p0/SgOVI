package es.uji.ei1027.ovi.model;

public class UsuarioOVI {
    private String dni;
    private String nom;
    private String cognoms;
    private String email;
    private int telefono;
    private String contrasenya;
    private Boolean consentimentInformat;
    private String estat;
    private String motiuRebuig;

    public UsuarioOVI() {}

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTelefono() { return telefono; }
    public void setTelefono(int telefono) { this.telefono = telefono; }

    public String getContrasenya() { return contrasenya; }
    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public Boolean getConsentimentInformat() { return consentimentInformat; }
    public void setConsentimentInformat(Boolean consentimentInformat) { this.consentimentInformat = consentimentInformat; }

    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }

    public String getMotiuRebuig() { return motiuRebuig; }
    public void setMotiuRebuig(String motiuRebuig) { this.motiuRebuig = motiuRebuig; }
}