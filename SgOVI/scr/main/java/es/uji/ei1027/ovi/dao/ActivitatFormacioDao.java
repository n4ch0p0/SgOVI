package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ActivitatFormacio;
import java.util.List;

public interface ActivitatFormacioDao {
    void addActivitat(ActivitatFormacio activitat);
    List<ActivitatFormacio> getActivitatsActives();
    void inscriureAsistente(int idActividad, String dniAsistente);
}