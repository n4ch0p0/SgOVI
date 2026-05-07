package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.Missatge;
import java.util.List;

public interface MissatgeDao {
    void addMissatge(Missatge missatge);
    List<Missatge> getMissatgesByConversa(int idConversa);
}