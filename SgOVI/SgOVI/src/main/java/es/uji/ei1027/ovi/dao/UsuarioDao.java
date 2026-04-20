package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import java.util.List;

public interface UsuarioDao {
    void addUsuario(UsuarioOVI usuario);
    void updateUsuario(UsuarioOVI usuario);
    UsuarioOVI getUsuario(String dni);
    List<UsuarioOVI> getUsuarios();
}