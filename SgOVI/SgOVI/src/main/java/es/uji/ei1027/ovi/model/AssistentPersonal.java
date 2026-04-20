package es.uji.ei1027.ovi.model;

public class AssistentPersonal {
    private String dni;
    private String nom;
    private String cognoms;
    private String email;
    private int telefono;
    private String contrasenya;
    private String tipus;
    private String formacioAcademica;
    private String experienciaPrevia;
    private String proximitatGeografica;
    private boolean actiu;
    private String estat;
    private String motiuRebuig;

    public AssistentPersonal() {}

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

    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }

    public String getFormacioAcademica() { return formacioAcademica; }
    public void setFormacioAcademica(String formacioAcademica) { this.formacioAcademica = formacioAcademica; }

    public String getExperienciaPrevia() { return experienciaPrevia; }
    public void setExperienciaPrevia(String experienciaPrevia) { this.experienciaPrevia = experienciaPrevia; }

    public String getProximitatGeografica() { return proximitatGeografica; }
    public void setProximitatGeografica(String proximitatGeografica) { this.proximitatGeografica = proximitatGeografica; }

    public boolean isActiu() { return actiu; }
    public void setActiu(boolean actiu) { this.actiu = actiu; }

    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }

    public String getMotiuRebuig() { return motiuRebuig; }
    public void setMotiuRebuig(String motiuRebuig) { this.motiuRebuig = motiuRebuig; }
}