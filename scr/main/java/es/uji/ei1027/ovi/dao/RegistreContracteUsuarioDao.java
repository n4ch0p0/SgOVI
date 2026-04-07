package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;

import java.util.List;

public interface RegistreContracteUsuarioDao {
    List<RegistreContracteUsuarioOvi> getContractesByUsuario(String dniUsuario);
}