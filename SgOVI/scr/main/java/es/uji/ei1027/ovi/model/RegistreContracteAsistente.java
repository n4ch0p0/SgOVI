package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class RegistreContracteAsistente {
    private int id;              // PK
    private int idRequest;       // FK: ID de la APRequest asociada
    private String dniPap;       // FK: DNI del Asistente contratado
    private LocalDate fecha_Inici;
    private LocalDate fecha_Fin;
    private String pdf_Path;


    public RegistreContracteAsistente() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getFecha_Inici() {
        return fecha_Inici;
    }

    public void setFecha_Inici(LocalDate fecha_Inici) {
        this.fecha_Inici = fecha_Inici;
    }

    public LocalDate getFecha_Fin() {
        return fecha_Fin;
    }

    public void setFecha_Fin(LocalDate fecha_Fin) {
        this.fecha_Fin = fecha_Fin;
    }

    public String getPdf_Path() {
        return pdf_Path;
    }

    public void setPdf_Path(String pdf_Path) {
        this.pdf_Path = pdf_Path;
    }
}
