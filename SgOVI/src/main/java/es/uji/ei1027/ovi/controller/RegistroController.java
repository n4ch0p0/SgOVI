package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.UsuarioDao;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private AsistenteDao asistenteDao;

    
    @GetMapping("/seleccion")
    public String opciones() {
        return "public/registro_opciones"; //
    }

    // Formulari d'Usuari
    @GetMapping("/usuario")
    public String formUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioOVI());
        return "public/registro_usuarios"; //
    }

    @PostMapping("/usuario/add")
    public String addUsuario(@ModelAttribute("usuario") UsuarioOVI usuario,
                             BindingResult bindingResult) {
        UsuarioValidator usuarioValidator = new UsuarioValidator();
        usuarioValidator.validate(usuario, bindingResult);
        if (bindingResult.hasErrors()) {
            return "public/registro_usuarios";
        }
        usuarioDao.addUsuario(usuario);
        return "redirect:/login?registrado=true";
    }

    // Formulari d'Assistent
    @GetMapping("/asistente")
    public String formAsistente(Model model) {
        model.addAttribute("asistente", new AssistentPersonal());
        return "public/registro_asistente"; //
    }

    @PostMapping("/asistente/add")
    public String addAsistente(@ModelAttribute("asistente") AssistentPersonal asistente,
                               BindingResult bindingResult) {
        AsistenteValidator asistenteValidator = new AsistenteValidator();
        asistenteValidator.validate(asistente, bindingResult);
        if (bindingResult.hasErrors()) {
            return "public/registro_asistente";
        }
        asistenteDao.addAsistente(asistente);
        return "redirect:/login?registrado=true";
    }
}
