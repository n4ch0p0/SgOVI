package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class Seleccion {
    private int idRequest;       // FK: ID de la APRequest
    private String dniPap;       // FK: DNI del AssitentPersonal
    private LocalDate fecha_Propuesta;
    // Getters y Setters...

    public Seleccion() {
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public String getDniPap() {
        return dniPap;
    }

    public void setDniPap(String dniPap) {
        this.dniPap = dniPap;
    }

    public LocalDate getFecha_Propuesta() {
        return fecha_Propuesta;
    }

    public void setFecha_Propuesta(LocalDate fecha_Propuesta) {
        this.fecha_Propuesta = fecha_Propuesta;
    }
}
