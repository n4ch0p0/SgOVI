package es.uji.ei1027.ovi.dao;

import java.util.List;

public interface CandidatPreassignatDao {
    void saveSeleccio(int idRequest, List<String> dniAps);
    List<String> getDniApsSeleccionats(int idRequest);
    void removeAssistentFromAllSeleccions(String dniAp);
}
