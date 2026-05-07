package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;
import java.time.LocalDate;
import java.util.List;

public interface RegistreContracteUsuarioDao {
    List<RegistreContracteUsuarioOvi> getContractesByUsuario(String dniUsuario);
    void addContracte(int idRequest, int idAp, LocalDate fechaInici, LocalDate fechaFin, String pdfPath);
    RegistreContracteUsuarioOvi getContracte(int id);
    void updateContracte(int id, LocalDate fechaInici, LocalDate fechaFin);
    List<RegistreContracteUsuarioOvi> getTodosLosContractes();
}