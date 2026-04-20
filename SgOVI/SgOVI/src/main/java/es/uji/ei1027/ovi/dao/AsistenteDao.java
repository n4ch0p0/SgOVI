package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import java.util.List;

public interface AsistenteDao {
    void addAsistente(AssistentPersonal asistente);
    void updateAsistente(AssistentPersonal asistente);
    void updateEstados(String dni, boolean estadoAceptado, boolean actiu, boolean estatAcceptat);
    AssistentPersonal getAsistente(String dni);
    List<AssistentPersonal> getAsistentes();
    List<AssistentPersonal> getAsistentesByTipus(String tipus);

    // --- MÉTODOS DE VALIDACIÓN ---
    List<AssistentPersonal> getAsistentesPendientes();
    void updateEstadoAsistente(String dni, boolean aceptado);

    List<AssistentPersonal> getCandidatosAdecuados(String tipus);
}