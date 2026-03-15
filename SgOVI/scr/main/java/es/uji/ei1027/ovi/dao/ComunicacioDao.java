package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ComunicacioUsuarioViPAP;
import java.util.List;

public interface ComunicacioDao {
    void addMensaje(ComunicacioUsuarioViPAP mensaje);
    List<ComunicacioUsuarioViPAP> getMensajesPorUsuario(String dniUsuario);
}