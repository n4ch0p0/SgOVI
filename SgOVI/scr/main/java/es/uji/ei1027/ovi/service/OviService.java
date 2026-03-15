package es.uji.ei1027.ovi.service;

import es.uji.ei1027.ovi.model.APRequest;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import java.util.List;

public interface OviService {
    // --- LOGIN Y REGISTRO ---
    UsuarioOVI loginUsuario(String dni, String password);
    AssistentPersonal loginAsistente(String dni, String password);
    void registrarUsuario(UsuarioOVI usuario);
    void registrarAsistente(AssistentPersonal asistente);

    // --- SOLICITUDES AP ---
    void solicitarAsistencia(APRequest request);
    List<APRequest> getSolicitudesUsuario(String dniUsuario);

    // --- PERFIL ---
    void actualizarUsuario(UsuarioOVI usuario);

    // --- CONTRACTES ---
    List<RegistreContracteUsuarioOvi> getContractesUsuario(String dniUsuario);
}