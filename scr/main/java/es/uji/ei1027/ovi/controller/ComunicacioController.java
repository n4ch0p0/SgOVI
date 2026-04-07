package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.ComunicacioDao;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comunicacio") // Esto hace que el botón deba apuntar a /comunicacio/...
public class ComunicacioController {

    private ComunicacioDao comunicacioDao;

    @Autowired
    public void setComunicacioDao(ComunicacioDao comunicacioDao) {
        this.comunicacioDao = comunicacioDao;
    }

    @GetMapping("/list") // La ruta completa será /comunicacio/list
    public String list(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuario");

        // Si no hay usuario en sesión, lo mandamos al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // Importante: que el nombre "missatges" coincida con el th:each del HTML
        model.addAttribute("missatges", comunicacioDao.getMensajesPorUsuario(usuario.getDni()));

        return "usuario/list_comunicacions"; // La ruta al archivo .html
    }
}