package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.*;
import es.uji.ei1027.ovi.dao.*;
import es.uji.ei1027.ovi.service.OviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;
import org.springframework.validation.BindingResult;

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
    private UsuarioDao usuarioDao;
    @Autowired
    private RegistreContracteUsuarioDao registreContracteUsuarioDao;
    @Autowired
    private UsuarioValidator usuarioValidator;

    @Autowired
    private APRequestValidator apRequestValidator;

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
    public String guardarPerfil(@ModelAttribute("usuario") UsuarioOVI usuarioActualizado,
                                BindingResult bindingResult, HttpSession session,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        UsuarioOVI usuarioSesion = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuarioSesion == null) return "redirect:/login";

        // Si l'usuari no introdueix contrasenya, mantenim la hash existent
        if (usuarioActualizado.getContrasenya() == null || usuarioActualizado.getContrasenya().trim().isEmpty()) {
            usuarioActualizado.setContrasenya(usuarioSesion.getContrasenya());
        }

        usuarioValidator.validate(usuarioActualizado, bindingResult);
        if (bindingResult.hasErrors()) {
            return "usuario/editar_perfil";
        }

        usuarioActualizado.setEstat(usuarioSesion.getEstat());
        usuarioActualizado.setConsentimentInformat(usuarioSesion.getConsentimentInformat());
        oviService.actualizarUsuario(usuarioActualizado);
        session.setAttribute("usuarioLogueado", usuarioActualizado);
        redirectAttributes.addFlashAttribute("mensajeExito", "El perfil s'ha actualitzat correctament.");
        return "redirect:/usuario/dashboard";
    }

    @GetMapping("/contractes")
    public String listarContractes(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("registreContracteUsuarioOvis", registreContracteUsuarioDao.getContractesByUsuario(usuario.getDni()));
        model.addAttribute("mapaNomsAssistents", asistenteDao.obtenerMapaNombresAsistentes());
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
    public String guardarSolicitud(@ModelAttribute("apRequest") APRequest peticion,
                                   BindingResult bindingResult, HttpSession session) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        // Fixem el DNI des de sessió (mai del formulari) i validem la resta
        peticion.setDniUsuario(usuario.getDni());
        apRequestValidator.validate(peticion, bindingResult);
        if (bindingResult.hasErrors()) {
            return "usuario/form_solicitud";
        }

        oviService.solicitarAsistencia(peticion);
        return "redirect:/usuario/solicitudes";
    }

    @GetMapping("/solicitudes/{idRequest}/candidatos")
    public String buscarCandidatos(@PathVariable int idRequest, HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        APRequest peticion = apRequestDao.getRequest(idRequest);

        // Defensem contra IDs inexistents (CRASH-1) i contra peticions alienes (SEC-3)
        if (peticion == null) return "redirect:/usuario/solicitudes";
        if (!usuario.getDni().equals(peticion.getDniUsuario())) return "redirect:/usuario/solicitudes";

        // Verificamos que la petición está aprobada
        if (!"Aprovada".equals(peticion.getEstat())) return "redirect:/usuario/solicitudes";

        List<AssistentPersonal> candidatos = asistenteDao.getCandidatosAdecuados(peticion.getTipusServei(), peticion.getPreferencies());

        model.addAttribute("peticion", peticion);
        model.addAttribute("candidatos", candidatos);
        return "usuario/proponer_candidatos";
    }

    @GetMapping("/contractes/editar/{id}")
    public String formEditarContracte(@PathVariable int id, HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        // Verificació de propietat: el contracte ha de pertànyer a l'usuari en sessió (SEC-3)
        boolean pertany = registreContracteUsuarioDao.getContractesByUsuario(usuario.getDni())
                .stream().anyMatch(c -> c.getId() == id);
        if (!pertany) return "redirect:/usuario/contractes";

        RegistreContracteUsuarioOvi contracte = registreContracteUsuarioDao.getContracte(id);
        if (contracte == null) return "redirect:/usuario/contractes";

        model.addAttribute("contracte", contracte);
        return "usuario/editar_contracte";
    }

    @PostMapping("/contractes/update")
    public String actualizarContracte(@RequestParam("id") int id,
                                      @RequestParam("dataInici") String inici,
                                      @RequestParam("dataFi") String fi, HttpSession session) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        // Verificació de propietat abans d'actualitzar (SEC-3)
        boolean pertany = registreContracteUsuarioDao.getContractesByUsuario(usuario.getDni())
                .stream().anyMatch(c -> c.getId() == id);
        if (!pertany) return "redirect:/usuario/contractes";

        registreContracteUsuarioDao.updateContracte(id, LocalDate.parse(inici),
                (fi != null && !fi.isEmpty()) ? LocalDate.parse(fi) : null);
        return "redirect:/usuario/contractes";
    }

    @PostMapping("/contractes/add")
    public String registrarContracte(@RequestParam("idRequest") int idRequest,
                                     @RequestParam("idAp") int idAp,
                                     @RequestParam("fechaInici") String fechaInici,
                                     @RequestParam(value = "fechaFin", required = false) String fechaFin,
                                     HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        LocalDate inici = LocalDate.parse(fechaInici);
        LocalDate fi = (fechaFin != null && !fechaFin.isEmpty()) ? LocalDate.parse(fechaFin) : null;

        registreContracteUsuarioDao.addContracte(idRequest, idAp, inici, fi, null);
        return "redirect:/usuario/contractes";
    }

    @PostMapping("/baja")
    public String darDeBaja(HttpSession session, RedirectAttributes redirectAttributes) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";
        
        usuarioDao.anonimizarUsuario(usuario.getDni());
        session.invalidate();
        
        redirectAttributes.addFlashAttribute("mensajeBaja", "El teu compte s'ha donat de baixa correctament. Les teues dades han sigut anonimitzades segons el RGPD.");
        return "redirect:/login";
    }
}