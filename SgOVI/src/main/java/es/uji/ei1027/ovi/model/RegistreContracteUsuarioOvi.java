package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class RegistreContracteUsuarioOvi {
    private int id;
    private String dniAsistente;
    private LocalDate dataInici;
    private LocalDate dataFi;
    private String estat;
    private String pdfPath;
    private String nomAssistent;
    private String nomUsuari;
    private String tipusServei;
    private String preferencies;

    public RegistreContracteUsuarioOvi() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDniAsistente() {
        return dniAsistente;
    }

    public void setDniAsistente(String dniAsistente) {
        this.dniAsistente = dniAsistente;
    }

    public LocalDate getDataInici() {
        return dataInici;
    }

    public void setDataInici(LocalDate dataInici) {
        this.dataInici = dataInici;
    }

    public LocalDate getDataFi() {
        return dataFi;
    }

    public void setDataFi(LocalDate dataFi) {
        this.dataFi = dataFi;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getNomAssistent() {
        return nomAssistent;
    }

    public void setNomAssistent(String nomAssistent) {
        this.nomAssistent = nomAssistent;
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

    @Override
    public String toString() {
        return "RegistreContracteUsuarioOvi{id='" + id + "'" +
                ", dniAsistente='" + dniAsistente + "'" +
                ", dataInici='" + dataInici + "'" +
                ", dataFi='" + dataFi + "'" +
                ", estat='" + estat + "'" +
                ", pdfPath='" + pdfPath + "'" +
                "}";
    }
}