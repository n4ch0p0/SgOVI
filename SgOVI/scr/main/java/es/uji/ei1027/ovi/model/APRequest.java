package es.uji.ei1027.ovi.model;

public class APRequest {
    private int id; // Generado automáticamente por la base de datos
    private String dniUsuario;
    private String tipusServei; // PAP o PATY
    private String preferencies;
    private String estat; // En Revisio, Aprovada, Rebutjada, Finalitzada

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDniUsuario() { return dniUsuario; }
    public void setDniUsuario(String dniUsuario) { this.dniUsuario = dniUsuario; }

    public String getTipusServei() { return tipusServei; }
    public void setTipusServei(String tipusServei) { this.tipusServei = tipusServei; }

    public String getPreferencies() { return preferencies; }
    public void setPreferencies(String preferencies) { this.preferencies = preferencies; }

    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }
}