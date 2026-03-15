package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import es.uji.ei1027.ovi.service.OviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private OviService oviService;

    @GetMapping({"/", "/login"})
    public String login(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") != null) return "redirect:/usuario/dashboard";
        if (session.getAttribute("asistenteLogueado") != null) return "redirect:/asistente/dashboard";
        if (session.getAttribute("tecnicLogueado") != null) return "redirect:/tecnic/dashboard";
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("username") String dni,
                                @RequestParam("password") String password,
                                HttpSession session,
                                Model model) {

        // 1. Técnico
        if (dni.equals("admin") && password.equals("admin")) {
            session.setAttribute("tecnicLogueado", true);
            return "redirect:/tecnic/dashboard";
        }

        // 2. Usuario OVI
        UsuarioOVI usuario = oviService.loginUsuario(dni, password);
        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/usuario/dashboard";
        }

        // 3. Asistente (USA EL NUEVO CAMPO DNI)
        AssistentPersonal asistente = oviService.loginAsistente(dni, password);
        if (asistente != null) {
            session.setAttribute("asistenteLogueado", asistente);
            return "redirect:/asistente/dashboard";
        }

        model.addAttribute("error", "DNI o contrasenya incorrectes.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}