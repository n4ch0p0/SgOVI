package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class RegistreContracteUsuarioOvi {
    private int id;
    private String dniAsistente;
    private LocalDate dataInici;
    private LocalDate dataFi;
    private String estat;
    private String pdfPath;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDniAsistente() { return dniAsistente; }
    public void setDniAsistente(String dniAsistente) { this.dniAsistente = dniAsistente; }

    public LocalDate getDataInici() { return dataInici; }
    public void setDataInici(LocalDate dataInici) { this.dataInici = dataInici; }

    public LocalDate getDataFi() { return dataFi; }
    public void setDataFi(LocalDate dataFi) { this.dataFi = dataFi; }

    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
}