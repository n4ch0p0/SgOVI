package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.*;
import es.uji.ei1027.ovi.dao.*;
import es.uji.ei1027.ovi.service.OviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private OviService oviService;
    @Autowired
    private APRequestDao apRequestDao;
    @Autowired
    private AsistenteDao asistenteDao;
    @Autowired
    private RegistreContracteUsuarioDao registreContracteUsuarioDao;

    // --- NUEVAS RUTAS DE CONTROL DE ACCESO ---
    @GetMapping("/espera")
    public String espera(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        return "usuario/espera"; // Tendrás que crear este HTML
    }

    @GetMapping("/rebutjat")
    public String rebutjat(HttpSession session, Model model) {
        UsuarioOVI u = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (u == null) return "redirect:/login";
        model.addAttribute("motiu", u.getMotiuRebuig());
        return "usuario/rebutjat"; // Tendrás que crear este HTML
    }

    @GetMapping("/dashboard")
    public String dashboardUsuario(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";
        if (!"Acceptat".equals(usuario.getEstat())) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "usuario/dashboard";
    }

    @GetMapping("/perfil")
    public String editarPerfil(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null || !"Acceptat".equals(usuario.getEstat())) return "redirect:/login";
        model.addAttribute("usuario", usuario);
        return "usuario/editar_perfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute("usuario") UsuarioOVI usuarioActualizado, HttpSession session) {
        UsuarioOVI usuarioSesion = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuarioSesion == null) return "redirect:/login";

        // Mantenemos datos que no se editan por seguridad
        usuarioActualizado.setEstat(usuarioSesion.getEstat());
        oviService.actualizarUsuario(usuarioActualizado);
        session.setAttribute("usuarioLogueado", usuarioActualizado);
        return "redirect:/usuario/dashboard";
    }

    @GetMapping("/contractes")
    public String listarContractes(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("registreContracteUsuarioOvis", oviService.getContractesUsuario(usuario.getDni()));
        return "usuario/list_contractes";
    }

    @GetMapping("/solicitudes")
    public String listarSolicitudes(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("solicitudes", oviService.getRequestsUsuario(usuario.getDni()));
        return "usuario/list_solicitudes";
    }

    @GetMapping("/solicitudes/nueva")
    public String formNuevaSolicitud(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        APRequest peticion = new APRequest();
        peticion.setDniUsuario(usuario.getDni());
        model.addAttribute("apRequest", peticion);
        return "usuario/form_solicitud";
    }

    @PostMapping("/solicitudes/add")
    public String guardarSolicitud(@ModelAttribute("apRequest") APRequest peticion, HttpSession session) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        peticion.setDniUsuario(usuario.getDni());
        oviService.solicitarAsistencia(peticion);
        return "redirect:/usuario/solicitudes";
    }

    // --- NUEVO: EL USUARIO BUSCA CANDIDATOS ---
    @GetMapping("/solicitudes/{idRequest}/candidatos")
    public String buscarCandidatos(@PathVariable int idRequest, HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        APRequest peticion = apRequestDao.getRequest(idRequest);

        // Verificamos que la petición es suya y está aprobada
        if (!"Aprovada".equals(peticion.getEstat())) return "redirect:/usuario/solicitudes";

        List<AssistentPersonal> candidatos = asistenteDao.getCandidatosAdecuados(peticion.getTipusServei());

        model.addAttribute("peticion", peticion);
        model.addAttribute("candidatos", candidatos);
        return "usuario/proponer_candidatos"; // Ahora esta vista pertenece al usuario
    }

    @GetMapping("/contractes/editar/{id}")
    public String formEditarContracte(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        model.addAttribute("contracte", registreContracteUsuarioDao.getContracte(id));
        return "usuario/editar_contracte";
    }

    @PostMapping("/contractes/update")
    public String actualizarContracte(@RequestParam("id") int id,
                                      @RequestParam("dataInici") String inici,
                                      @RequestParam("dataFi") String fi, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        registreContracteUsuarioDao.updateContracte(id, LocalDate.parse(inici),
                (fi != null && !fi.isEmpty()) ? LocalDate.parse(fi) : null);
        return "redirect:/usuario/contractes";
    }
}