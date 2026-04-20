package es.uji.ei1027.ovi.service;

import es.uji.ei1027.ovi.model.APRequest;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import java.util.List;

public interface OviService {
    UsuarioOVI loginUsuario(String dni, String password);
    AssistentPersonal loginAsistente(String dni, String password);
    void registrarUsuario(UsuarioOVI usuario);
    void registrarAsistente(AssistentPersonal asistente);

    void solicitarAsistencia(APRequest request);
    List<APRequest> getRequestsUsuario(String dniUsuario);
    void actualizarUsuario(UsuarioOVI usuario);

    List<RegistreContracteUsuarioOvi> getContractesUsuario(String dniUsuario);
}