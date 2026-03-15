package es.uji.ei1027.ovi.model;

import java.time.LocalDateTime;

public class Mensaje {
    private int idComunicacio;
    private int idSeleccion;
    private String texto;
    private LocalDateTime fecha;
    private String nombreAsistente; // Para saber con quién habla

    public Mensaje() {
    }

    public int getIdComunicacio() {
        return idComunicacio;
    }

    public void setIdComunicacio(int idComunicacio) {
        this.idComunicacio = idComunicacio;
    }

    public int getIdSeleccion() {
        return idSeleccion;
    }

    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNombreAsistente() {
        return nombreAsistente;
    }

    public void setNombreAsistente(String nombreAsistente) {
        this.nombreAsistente = nombreAsistente;
    }
}