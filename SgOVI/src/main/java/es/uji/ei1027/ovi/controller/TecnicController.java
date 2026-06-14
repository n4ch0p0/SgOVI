package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.*;
import es.uji.ei1027.ovi.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Controller
@RequestMapping("/tecnic")
public class TecnicController {

    @Autowired
    private TecnicDao tecnicDao;
    @Autowired
    private UsuarioDao usuarioDao; // Asegúrate de tenerlo inyectado
    @Autowired
    private AsistenteDao asistenteDao;
    @Autowired
    private APRequestDao apRequestDao;
    @Autowired
    private RegistreContracteUsuarioDao registreContracteUsuarioDao;
    @Autowired
    private ConversaDao conversaDao;
    @Autowired
    private CandidatPreassignatDao candidatPreassignatDao;
    @Autowired
    private RegistreContracteAsistenteDao registreContracteAsistenteDao;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        return "tecnic/dashboard";
    }

    @GetMapping("/usuarios")
    public String listarTodosLosUsuarios(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("usuarios", usuarioDao.getUsuarios());
        return "tecnic/list_usuarios";
    }

    @GetMapping("/usuarios/{dni}/projecte-vida")
    public String veureProjecteVidaUsuari(@PathVariable String dni, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        UsuarioOVI client = usuarioDao.getUsuario(dni);
        if (client == null) return "redirect:/tecnic/usuarios";
        model.addAttribute("nomUsuari", client.getNom() + " " + client.getCognoms());
        model.addAttribute("projecteVida", client.getProjecteVida());
        model.addAttribute("backUrl", "/tecnic/usuarios");
        return "usuario/projecte_vida_readonly";
    }

    @GetMapping("/asistentes")
    public String listarTodosLosAsistentes(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("asistentes", asistenteDao.getAsistentes());
        return "tecnic/list_asistentes";
    }

    @GetMapping("/validar-usuarios")
    public String listarUsuariosPendientes(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("usuarios", tecnicDao.getUsuariosPendientes());
        return "tecnic/validar_usuarios";
    }

    @PostMapping("/validar-usuario")
    public String decidirUsuario(@RequestParam("dni") String dni,
                                 @RequestParam("aceptado") boolean aceptado,
                                 @RequestParam(value = "motiu", required = false) String motiu,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        String estado = aceptado ? "Acceptat" : "Rebutjat";
        tecnicDao.actualizarEstadoUsuario(dni, estado, motiu);
        redirectAttributes.addFlashAttribute("mensajeExito", "S'ha canviat l'estat de l'usuari a " + estado + ".");
        return "redirect:/tecnic/validar-usuarios";
    }

    @GetMapping("/validar-asistentes")
    public String listarAsistentesPendientes(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("asistentes", asistenteDao.getAsistentesPendientes());
        return "tecnic/validar_asistentes";
    }

    @PostMapping("/decidir-asistente")
    public String decidirAsistente(@RequestParam("dni") String dni,
                                   @RequestParam("aceptado") boolean aceptado,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        asistenteDao.updateEstadoAsistente(dni, aceptado);
        String text = aceptado ? "Acceptat" : "Rebutjat";
        redirectAttributes.addFlashAttribute("mensajeExito", "S'ha canviat l'estat de l'assistent a " + text + ".");
        return "redirect:/tecnic/validar-asistentes";
    }

    @GetMapping("/solicitudes")
    public String gestionarSolicitudes(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("solicitudes", apRequestDao.getRequestsPendientes());
        model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
        return "tecnic/gestionar_solicitudes";
    }

    @GetMapping("/confirmar-solicitud/{id}")
    public String confirmarSolicitud(@PathVariable int id, @RequestParam String accio,
                                     HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        APRequest peticion = apRequestDao.getRequest(id);
        if (peticion == null) return "redirect:/tecnic/solicitudes";
        String nomUsuari = usuarioDao.obtenerMapaNombresUsuarios()
                .getOrDefault(peticion.getDniUsuario(), peticion.getDniUsuario());
        model.addAttribute("peticion", peticion);
        model.addAttribute("nomUsuari", nomUsuari);
        model.addAttribute("accio", accio);
        return "tecnic/confirmar_solicitud";
    }

    @PostMapping("/aprobar-solicitud")
    public String aprobarSolicitud(@RequestParam("idRequest") int idRequest,
                                   @RequestParam("aceptada") boolean aceptada,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        String estado = aceptada ? "Aprovada" : "Rebutjada";
        APRequest request = apRequestDao.getRequest(idRequest);
        String nomUsuari = request != null
                ? usuarioDao.obtenerMapaNombresUsuarios().getOrDefault(request.getDniUsuario(), request.getDniUsuario())
                : "l'usuari";
        apRequestDao.updateEstado(idRequest, estado);
        redirectAttributes.addFlashAttribute("mensajeExito", "La sol·licitud de " + nomUsuari + " s'ha marcat com a " + estado + ".");
        return "redirect:/tecnic/solicitudes";
    }

    @GetMapping("/todos-contractes")
    public String listTodosContractes(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("contractes", registreContracteUsuarioDao.getTodosLosContractes());
        return "tecnic/list_todos_contractes";
    }

    @GetMapping("/todos-contractes/{id}/veure")
    public String veureContracte(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        RegistreContracteUsuarioOvi contracte = registreContracteUsuarioDao.getContracte(id);
        if (contracte == null) return "redirect:/tecnic/todos-contractes";
        model.addAttribute("contracte", contracte);
        model.addAttribute("backUrl", "/tecnic/todos-contractes");
        return "usuario/veure_contracte";
    }

    @GetMapping("/negociacions")
    public String llistarNegociacions(HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("solicitudes", apRequestDao.getRequestsEnNegociacio());
        model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
        return "tecnic/list_negociacions";
    }

    @GetMapping("/negociacions/{idRequest}")
    public String consultarNegociacions(@PathVariable int idRequest, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";

        APRequest request = apRequestDao.getRequest(idRequest);
        String nomUsuari = "";
        if (request != null) {
            nomUsuari = usuarioDao.obtenerMapaNombresUsuarios()
                    .getOrDefault(request.getDniUsuario(), request.getDniUsuario());
        }

        model.addAttribute("idRequest", idRequest);
        model.addAttribute("nomUsuari", nomUsuari);
        model.addAttribute("converses", conversaDao.getConversesByRequest(idRequest));
        return "tecnic/consultar_negociacions";
    }

    @GetMapping("/solicitudes/{idRequest}/candidatos")
    public String verCandidatsSolicitud(@PathVariable int idRequest, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";

        APRequest peticion = apRequestDao.getRequest(idRequest);
        if (peticion == null) return "redirect:/tecnic/solicitudes";

        List<AssistentPersonal> tots = asistenteDao.getCandidatosAdecuados(
                peticion.getTipusServei(), peticion.getPreferencies());
        List<String> seleccioActual = candidatPreassignatDao.getDniApsSeleccionats(idRequest);

        List<AssistentPersonal> candidatos;
        if (seleccioActual.isEmpty()) {
            candidatos = tots;
        } else {
            Set<String> seleccioSet = new HashSet<>(seleccioActual);
            candidatos = Stream.concat(
                    tots.stream().filter(ap ->  seleccioSet.contains(ap.getDni())),
                    tots.stream().filter(ap -> !seleccioSet.contains(ap.getDni()))
            ).toList();
        }

        model.addAttribute("peticion", peticion);
        model.addAttribute("candidatos", candidatos);
        model.addAttribute("seleccioActual", seleccioActual);
        model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
        return "tecnic/candidatos_solicitud";
    }

    // --- Baixa d'usuari ---

    @GetMapping("/baixa-usuari/{dni}")
    public String confirmarBaixaUsuari(@PathVariable String dni, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("persona", usuarioDao.getUsuario(dni));
        model.addAttribute("teContracte", registreContracteUsuarioDao.teContracteActiu(dni));
        model.addAttribute("requestsActives", apRequestDao.teRequestsActives(dni));
        return "tecnic/confirmar_baixa_usuari";
    }

    @PostMapping("/baixa-usuari/{dni}")
    public String executarBaixaUsuari(@PathVariable String dni, HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        if (registreContracteUsuarioDao.teContracteActiu(dni)) {
            redirectAttributes.addFlashAttribute("errorBaixa", "No es pot donar de baixa: té un contracte actiu.");
            return "redirect:/tecnic/usuarios";
        }
        apRequestDao.tancarRequestsActivesPerUsuari(dni);
        usuarioDao.anonimizarUsuario(dni);
        redirectAttributes.addFlashAttribute("mensajeExito", "L'usuari ha sigut donat de baixa correctament.");
        return "redirect:/tecnic/usuarios";
    }

    // --- Baixa d'assistent ---

    @GetMapping("/baixa-assistent/{dni}")
    public String confirmarBaixaAssistent(@PathVariable String dni, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        model.addAttribute("persona", asistenteDao.getAsistente(dni));
        model.addAttribute("teContracte", registreContracteAsistenteDao.teContracteActiu(dni));
        return "tecnic/confirmar_baixa_assistent";
    }

    @PostMapping("/baixa-assistent/{dni}")
    public String executarBaixaAssistent(@PathVariable String dni, HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        if (registreContracteAsistenteDao.teContracteActiu(dni)) {
            redirectAttributes.addFlashAttribute("errorBaixa", "No es pot donar de baixa: té un contracte actiu.");
            return "redirect:/tecnic/asistentes";
        }
        candidatPreassignatDao.removeAssistentFromAllSeleccions(dni);
        asistenteDao.anonimizarAsistente(dni);
        redirectAttributes.addFlashAttribute("mensajeExito", "L'assistent ha sigut donat de baixa correctament.");
        return "redirect:/tecnic/asistentes";
    }

    @PostMapping("/solicitudes/{idRequest}/candidatos/guardar")
    public String guardarSeleccio(@PathVariable int idRequest,
                                  @RequestParam(value = "dniAps", required = false) List<String> dniAps,
                                  HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        candidatPreassignatDao.saveSeleccio(idRequest, dniAps != null ? dniAps : Collections.emptyList());
        redirectAttributes.addFlashAttribute("mensajeExito", "Selecció de candidats guardada correctament.");
        return "redirect:/tecnic/solicitudes/" + idRequest + "/candidatos";
    }
}