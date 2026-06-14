package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class RegistreContracteAsistente {
    private int idSeleccion; // Mantenim el nom per compatibilitat amb l'HTML
    private LocalDate fechaInici;
    private LocalDate fechaFin;
    private String dniUsuario;
    private String pdfPath;
    private String estat;
    private String nomUsuari;
    private String tipusServei;
    private String preferencies;
    private int idRequest;

    public RegistreContracteAsistente() {
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public LocalDate getFechaInici() {
        return fechaInici;
    }

    public void setFechaInici(LocalDate fechaInici) {
        this.fechaInici = fechaInici;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDniUsuario() {
        return dniUsuario;
    }

    public void setDniUsuario(String dniUsuario) {
        this.dniUsuario = dniUsuario;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getTipusServei() {
        return tipusServei;
    }

    public void setTipusServei(String tipusServei) {
        this.tipusServei = tipusServei;
    }

    public String getPreferencies() {
        return preferencies;
    }

    public void setPreferencies(String preferencies) {
        this.preferencies = preferencies;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    @Override
    public String toString() {
        return "RegistreContracteAsistente{idSeleccion='" + idSeleccion + "'" +
                ", fechaInici='" + fechaInici + "'" +
                ", fechaFin='" + fechaFin + "'" +
                ", dniUsuario='" + dniUsuario + "'" +
                ", pdfPath='" + pdfPath + "'" +
                "}";
    }
}