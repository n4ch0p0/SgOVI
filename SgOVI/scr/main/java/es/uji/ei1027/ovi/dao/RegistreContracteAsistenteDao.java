package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.RegistreContracteAsistente;
import java.util.List;

public interface RegistreContracteAsistenteDao {
    void addContracte(RegistreContracteAsistente contracte);
    RegistreContracteAsistente getContracte(int id);
    List<RegistreContracteAsistente> getContractesByPap(String dniPap);
    List<RegistreContracteAsistente> getContractesByRequest(int idRequest);
}