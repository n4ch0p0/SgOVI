package es.uji.ei1027.ovi.service;

import es.uji.ei1027.ovi.dao.*;
import es.uji.ei1027.ovi.model.*;
import org.mindrot.jbcrypt.BCrypt;
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

    @Override
    public void registrarUsuario(UsuarioOVI usuario) {
        usuarioDao.addUsuario(usuario);
    }

    @Override
    public void registrarAsistente(AssistentPersonal asistente) {
        asistenteDao.addAsistente(asistente);
    }

    @Override
    public void actualizarUsuario(UsuarioOVI usuario) {
        usuarioDao.updateUsuario(usuario);
    }

    @Override
    public UsuarioOVI loginUsuario(String dni, String password) {
        UsuarioOVI u = usuarioDao.getUsuario(dni);
        if (u != null) {
            try {
                if (BCrypt.checkpw(password, u.getContrasenya())) return u;
            } catch (IllegalArgumentException e) {
                // Fallback para contraseñas en texto plano de prueba en la BD
                if (u.getContrasenya().equals(password)) return u;
            }
        }
        return null;
    }

    @Override
    public AssistentPersonal loginAsistente(String dni, String password) {
        AssistentPersonal a = asistenteDao.getAsistente(dni);
        if (a != null) {
            try {
                if (BCrypt.checkpw(password, a.getContrasenya())) return a;
            } catch (IllegalArgumentException e) {
                if (a.getContrasenya().equals(password)) return a;
            }
        }
        return null;
    }

    @Override
    public void solicitarAsistencia(APRequest request) {
        apRequestDao.addRequest(request);
    }

    @Override
    public List<APRequest> getRequestsUsuario(String dniUsuario) {
        return apRequestDao.getRequestsByUsuario(dniUsuario);
    }

    @Override
    public List<RegistreContracteUsuarioOvi> getContractesUsuario(String dniUsuario) {
        return registreContracteUsuarioDao.getContractesByUsuario(dniUsuario);
    }
}