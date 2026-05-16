package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.MissatgeTecnic;
import java.util.List;

public interface MissatgeTecnicDao {
    void addMissatge(MissatgeTecnic missatge);
    List<MissatgeTecnic> getMissatgesByConversa(int idConversaTecnic);
}
