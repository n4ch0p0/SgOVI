package es.uji.ei1027.ovi.service;

import es.uji.ei1027.ovi.dao.APRequestDao;
import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.RegistreContracteUsuarioDao;
import es.uji.ei1027.ovi.dao.UsuarioDao;
import es.uji.ei1027.ovi.model.APRequest;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OviServiceImpl implements OviService {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private AsistenteDao asistenteDao;

    @Autowired
    private APRequestDao apRequestDao;

    @Autowired
    private RegistreContracteUsuarioDao registreContracteUsuarioDao;

    // --- LOGIN Y REGISTRO ---
    @Override
    public UsuarioOVI loginUsuario(String dni, String password) {
        UsuarioOVI usuario = usuarioDao.getUsuario(dni);
        if (usuario != null && usuario.getContrasenya().equals(password)) {
            return usuario;
        }
        return null;
    }

    public AssistentPersonal loginAsistente(String dni, String password) {
        AssistentPersonal a = asistenteDao.getAsistente(dni); // Este busca por DNI en la tabla
        if (a != null && a.getDni().equals(password)) { // Tu requisito: Password = DNI
            return a;
        }
        return null;
    }

    @Override
    public void registrarUsuario(UsuarioOVI usuario) {
        usuarioDao.addUsuario(usuario);
    }

    @Override
    public void registrarAsistente(AssistentPersonal asistente) {
        asistenteDao.addAsistente(asistente);
    }


    @Override
    public void solicitarAsistencia(APRequest request) {
        apRequestDao.addRequest(request);
    }

    @Override
    public List<APRequest> getSolicitudesUsuario(String dniUsuario) {
        return apRequestDao.getRequestsByUsuario(dniUsuario);
    }

    @Override
    public void actualizarUsuario(UsuarioOVI usuario) {
        usuarioDao.updateUsuario(usuario);
    }

    @Override
    public List<RegistreContracteUsuarioOvi> getContractesUsuario(String dniUsuario) {
        return registreContracteUsuarioDao.getContractesByUsuario(dniUsuario);
    }
}