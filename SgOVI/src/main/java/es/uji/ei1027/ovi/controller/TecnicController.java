package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.*;
import es.uji.ei1027.ovi.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String gestionarSolicitudes(Model model) {
        model.addAttribute("solicitudes", apRequestDao.getRequestsPendientes());
        model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
        return "tecnic/gestionar_solicitudes";
    }

    @PostMapping("/aprobar-solicitud")
    public String aprobarSolicitud(@RequestParam("idRequest") int idRequest,
                                   @RequestParam("aceptada") boolean aceptada,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";
        String estado = aceptada ? "Aprovada" : "Rebutjada";
        apRequestDao.updateEstado(idRequest, estado);
        redirectAttributes.addFlashAttribute("mensajeExito", "La sol·licitud s'ha marcat com a " + estado + ".");
        return "redirect:/tecnic/solicitudes";
    }

    @GetMapping("/contractes")
    public String listTodosContractes(Model model) {
        model.addAttribute("contractes", registreContracteUsuarioDao.getTodosLosContractes());
        model.addAttribute("mapaNomsClientsPorContracte", registreContracteUsuarioDao.obtenerMapaNombresUsuariosPorContracte());
        model.addAttribute("mapaNomsAssistents", asistenteDao.obtenerMapaNombresAsistentes());
        return "tecnic/list_todos_contractes";
    }

    @GetMapping("/negociacions/{idRequest}")
    public String consultarNegociacions(@PathVariable int idRequest, HttpSession session, Model model) {
        if (session.getAttribute("tecnicLogueado") == null) return "redirect:/login";

        model.addAttribute("idRequest", idRequest);
        model.addAttribute("converses", conversaDao.getConversesByRequest(idRequest));
        return "tecnic/consultar_negociacions";
    }
}