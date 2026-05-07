package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.RegistreContracteAsistente;
import java.util.List;

public interface RegistreContracteAsistenteDao {
    List<RegistreContracteAsistente> getContractesByAsistente(String dniPap);
}