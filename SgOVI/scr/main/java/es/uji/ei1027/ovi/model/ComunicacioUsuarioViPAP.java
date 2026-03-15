package es.uji.ei1027.ovi.model;

import java.time.LocalDate;

public class ComunicacioUsuarioViPAP {
    private int id;              // id_comunicacio en BD
    private int idSeleccion;     // id_seleccion en BD (Esto es lo más importante)
    private String mensaje;      // mensaje en BD
    private LocalDate fecha;     // fecha en BD

    public ComunicacioUsuarioViPAP() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdSeleccion() { return idSeleccion; }
    public void setIdSeleccion(int idSeleccion) { this.idSeleccion = idSeleccion; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}