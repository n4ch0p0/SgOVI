package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.APRequest;
import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import es.uji.ei1027.ovi.service.OviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private OviService oviService;

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String dashboardUsuario(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "usuario/dashboard";
    }

    // --- LISTA DE SUS SOLICITUDES ---
    @GetMapping("/solicitudes")
    public String listarSolicitudes(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        // Buscamos sus solicitudes en la base de datos
        model.addAttribute("solicitudes", oviService.getSolicitudesUsuario(usuario.getDni()));
        return "usuario/list_solicitudes";
    }

    // --- FORMULARIO PARA CREAR NUEVA SOLICITUD ---
    @GetMapping("/solicitudes/nueva")
    public String formNuevaSolicitud(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        APRequest peticion = new APRequest();
        peticion.setDniUsuario(usuario.getDni()); // Pre-asignamos el DNI del usuario logueado

        model.addAttribute("apRequest", peticion);
        return "usuario/form_solicitud";
    }

    // --- GUARDAR LA SOLICITUD ---
    @PostMapping("/solicitudes/add")
    public String guardarSolicitud(APRequest peticion, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        oviService.solicitarAsistencia(peticion);
        return "redirect:/usuario/solicitudes"; // Lo mandamos de vuelta a la lista
    }

    // --- EDITAR PERFIL ---
    @GetMapping("/perfil/editar")
    public String pantallaEditarPerfil(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "usuario/editar_perfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(UsuarioOVI usuarioEditado, HttpSession session) {
        UsuarioOVI usuarioActual = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuarioActual == null) return "redirect:/login";

        // Por seguridad, obligamos a que el DNI sea el del usuario logueado
        // (para que nadie pueda cambiar el DNI hackeando el formulario)
        usuarioEditado.setDni(usuarioActual.getDni());

        // Guardamos en BD
        oviService.actualizarUsuario(usuarioEditado);

        // Actualizamos la sesión con los nuevos datos para que el dashboard muestre los cambios
        session.setAttribute("usuarioLogueado", usuarioEditado);

        return "redirect:/usuario/dashboard";
    }

    // --- CONTRACTES ---
    @GetMapping("/contractes")
    public String listarContractes(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        List<RegistreContracteUsuarioOvi> registreContracteUsuarioOvis = oviService.getContractesUsuario(usuario.getDni());
        model.addAttribute("contractes", registreContracteUsuarioOvis);

        return "usuario/list_contractes";
    }
}