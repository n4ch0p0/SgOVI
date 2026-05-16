package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.dao.ActivitatFormacioDao;
import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.RegistreContracteAsistenteDao;
import es.uji.ei1027.ovi.dao.UsuarioDao;
import es.uji.ei1027.ovi.dao.APRequestDao;
import es.uji.ei1027.ovi.model.ActivitatFormacio;
import es.uji.ei1027.ovi.model.Missatge;
import es.uji.ei1027.ovi.model.MissatgeTecnic;
import es.uji.ei1027.ovi.model.RegistreContracteAsistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/asistente")
public class AsistenteController {

    @Autowired
    private AsistenteDao asistenteDao;

    @Autowired
    private RegistreContracteAsistenteDao registreContracteAsistenteDao;

    @Autowired
    private ActivitatFormacioDao activitatFormacioDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private APRequestDao apRequestDao;

    // PÀGINA D'ESPERA PER ALS PENDENTS
    @GetMapping("/espera")
    public String espera(HttpSession session) {
        if (session.getAttribute("asistenteLogueado") == null) return "redirect:/login";
        return "asistente/espera";
    }

    // PÀGINA DE REBUIG
    @GetMapping("/rebutjat")
    public String rebutjat(HttpSession session, Model model) {
        AssistentPersonal a = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (a == null) return "redirect:/login";
        model.addAttribute("motiu", a.getMotiuRebuig());
        return "asistente/rebutjat";
    }

    // EL DASHBOARD PRINCIPAL
    @RequestMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        // Seguretat extra: només entren els acceptats
        if (!"Acceptat".equals(asistente.getEstat())) return "redirect:/login";

        model.addAttribute("asistente", asistente);
        // We also need the maps here in case the dashboard shows conversations directly
        model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
        model.addAttribute("mapaNomsRequests", apRequestDao.obtenerMapaNombresRequests());

        return "asistente/dashboard";
    }

    // 1. EL BOTÓ "EL MEU PERFIL"
    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null || !"Acceptat".equals(asistente.getEstat())) return "redirect:/login";

        model.addAttribute("asistente", asistente);
        return "asistente/perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute("asistente") AssistentPersonal asistenteActualizado, HttpSession session, RedirectAttributes redirectAttributes) {
        AssistentPersonal asistenteSesion = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistenteSesion == null) return "redirect:/login";

        // Per seguretat, mantenim les dades crítiques i estats que no s'editen al formulari
        asistenteActualizado.setDni(asistenteSesion.getDni());
        asistenteActualizado.setTipus(asistenteSesion.getTipus());
        asistenteActualizado.setEstat(asistenteSesion.getEstat());
        asistenteActualizado.setMotiuRebuig(asistenteSesion.getMotiuRebuig());
        asistenteActualizado.setActiu(asistenteSesion.isActiu());

        asistenteDao.updateAsistente(asistenteActualizado);
        session.setAttribute("asistenteLogueado", asistenteActualizado);

        redirectAttributes.addFlashAttribute("mensajeExito", "El teu perfil s'ha actualitzat correctament.");
        return "redirect:/asistente/dashboard";
    }

    // 2. EL BOTÓ DE "GESTIONAR CONTRACTES"
    @GetMapping("/contractes")
    public String verContractes(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        model.addAttribute("registreContracteUsuarioOvis", registreContracteAsistenteDao.getContractesByAsistente(asistente.getDni()));
        model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
        return "asistente/contractes";
    }

    // 3. EL BOTÓ "ACTIVITATS DE FORMACIÓ"
    @GetMapping("/formacio")
    public String verFormacio(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        model.addAttribute("activitats", activitatFormacioDao.getActivitatsActives());
        model.addAttribute("mapaNomsFormadors", activitatFormacioDao.obtenerMapaNombresFormadores());
        return "asistente/formacio";
    }

    @PostMapping("/formacio/inscriure")
    public String inscriureFormacio(@RequestParam("idActividad") int idActividad, HttpSession session, RedirectAttributes redirectAttributes) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        activitatFormacioDao.inscriureAsistente(idActividad, asistente.getDni());
        redirectAttributes.addFlashAttribute("mensajeExito", "T'has inscrit a l'activitat de formació amb èxit!");
        return "redirect:/asistente/formacio";
    }

    // 4. EL BOTÓ "VEURE MISSATGES" (Ara redirigeix al nou sistema de converses)
    @GetMapping("/missatges")
    public String verMensajes(HttpSession session) {
        if (session.getAttribute("asistenteLogueado") == null) return "redirect:/login";
        // Redirigim al controlador global de converses que vam crear a la fase 3
        return "redirect:/conversa/list";
    }
}