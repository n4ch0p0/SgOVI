package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.UsuarioDao;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private AsistenteDao asistenteDao;

    // Ruta inicial: /registro/seleccion
    @GetMapping("/seleccion")
    public String opciones() {
        return "public/registro_opciones"; //
    }

    // Formulario de Usuario
    @GetMapping("/usuario")
    public String formUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioOVI());
        return "public/registro_usuarios"; //
    }

    @PostMapping("/usuario/add")
    public String addUsuario(@ModelAttribute("usuario") UsuarioOVI usuario) {
        usuarioDao.addUsuario(usuario);
        return "redirect:/login?registrado=true";
    }

    // Formulario de Asistente
    @GetMapping("/asistente")
    public String formAsistente(Model model) {
        model.addAttribute("asistente", new AssistentPersonal());
        return "public/registro_asistente"; //
    }

    @PostMapping("/asistente/add")
    public String addAsistente(@ModelAttribute("asistente") AssistentPersonal asistente) {
        // Guardamos en la tabla con el nuevo campo DNI
        asistenteDao.addAsistente(asistente);

        // Al redirigir a /login, el usuario podrá entrar usando su DNI en ambos campos
        return "redirect:/login?registrado=true";
    }
}
