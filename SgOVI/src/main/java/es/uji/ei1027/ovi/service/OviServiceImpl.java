package es.uji.ei1027.ovi.service;

import es.uji.ei1027.ovi.dao.*;
import es.uji.ei1027.ovi.model.*;
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
        if (u != null && u.getContrasenya().equals(password)) return u;
        return null;
    }

    @Override
    public AssistentPersonal loginAsistente(String dni, String password) {
        AssistentPersonal a = asistenteDao.getAsistente(dni);
        if (a != null && a.getDni().equals(password)) return a;
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