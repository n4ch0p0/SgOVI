package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class ActivitatFormacio {
    private int id;              // PK
    private String dniFormador;  // FK: DNI del Formador responsable
    private String titol;
    private LocalDate fecha;
    private String tipus;        // formacio o divulgacio

    public ActivitatFormacio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDniFormador() {
        return dniFormador;
    }

    public void setDniFormador(String dniFormador) {
        this.dniFormador = dniFormador;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }
}
