package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.Conversa;
import java.util.List;

public interface ConversaDao {
    void addConversa(int idRequest, int idAp);
    List<Conversa> getConversesByUsuario(String dniUsuario);
    List<Conversa> getConversesByAp(String dniAp);
    void addConversaDni(int idRequest, String dniAp);

    boolean existeixConversa(int idRequest, String dniAp);

    List<Conversa> getConversesByRequest(int idRequest);
}