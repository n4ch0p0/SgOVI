package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ActivitatFormacio;
import java.util.List;
import java.util.Map;

public interface ActivitatFormacioDao {
    void addActivitat(ActivitatFormacio activitat);
    List<ActivitatFormacio> getActivitatsActives();
    void inscriureAsistente(int idActividad, String dniAsistente);
    Map<String, String> obtenerMapaNombresFormadores();
}