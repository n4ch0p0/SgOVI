package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ConversaTecnic;
import java.util.List;

public interface ConversaTecnicDao {
    void addConversa(String dniUsuario);
    List<ConversaTecnic> getConversesByUsuario(String dniUsuario);
    List<ConversaTecnic> getAllConverses();
    boolean existeixConversa(String dniUsuario);
}
