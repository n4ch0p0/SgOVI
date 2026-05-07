package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class RegistreContracteAsistente {
    private int idSeleccion; // Mantenim el nom per compatibilitat amb l'HTML
    private LocalDate fechaInici;
    private LocalDate fechaFin;
    private String dniUsuario; // Ara guarda el DNI del client
    private String pdfPath;

    public RegistreContracteAsistente() {}

    public int getIdSeleccion() { return idSeleccion; }
    public void setIdSeleccion(int idSeleccion) { this.idSeleccion = idSeleccion; }

    public LocalDate getFechaInici() { return fechaInici; }
    public void setFechaInici(LocalDate fechaInici) { this.fechaInici = fechaInici; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getDniUsuario() { return dniUsuario; }
    public void setDniUsuario(String dniUsuario) { this.dniUsuario = dniUsuario; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
}