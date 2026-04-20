package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.APRequest;
import java.util.List;

public interface APRequestDao {
    void addRequest(APRequest request);
    List<APRequest> getRequestsByUsuario(String dniUsuario);
    List<APRequest> getRequestsPendientes();
    void updateEstado(int id, String estat);

    APRequest getRequest(int idRequest);
}