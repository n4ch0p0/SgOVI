package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import java.util.List;

public interface TecnicDao {
    // Listar usuarios que están en estado 'Pendent'
    List<UsuarioOVI> getUsuariosPendientes();

    // Actualizar estado a 'Acceptat' o 'Rebutjat'
    void actualizarEstadoUsuario(String dni, String nuevoEstado, String motivoRechazo);
}