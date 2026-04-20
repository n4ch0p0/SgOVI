package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.ActivitatFormacioDao;
import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.RegistreContracteAsistenteDao;
import es.uji.ei1027.ovi.model.ActivitatFormacio;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.RegistreContracteAsistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/asistente")
public class AsistenteController {

    @Autowired
    private AsistenteDao asistenteDao;

    @Autowired
    private RegistreContracteAsistenteDao registreContracteAsistenteDao;

    @Autowired
    private ActivitatFormacioDao activitatFormacioDao;

    // EL DASHBOARD PRINCIPAL
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        // Seguretat extra: només entren els acceptats
        if (!"Acceptat".equals(asistente.getEstat())) return "redirect:/login";

        model.addAttribute("asistente", asistente);
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
    public String actualizarPerfil(@ModelAttribute("asistente") AssistentPersonal asistenteActualizado, HttpSession session) {
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

        return "redirect:/asistente/dashboard";
    }

    // 2. EL BOTÓ DE "GESTIONAR CONTRACTES"
    @GetMapping("/contractes")
    public String verContractes(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        List<RegistreContracteAsistente> misContractes = registreContracteAsistenteDao.getContractesByAsistente(asistente.getDni());
        model.addAttribute("registreContracteUsuarioOvis", misContractes);

        return "asistente/contractes";
    }

    // 3. EL BOTÓ "ACTIVITATS DE FORMACIÓ"
    @GetMapping("/formacio")
    public String verFormacio(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        List<ActivitatFormacio> llistaActivitats = activitatFormacioDao.getActivitatsActives();
        model.addAttribute("activitats", llistaActivitats);

        return "asistente/formacio";
    }

    @PostMapping("/formacio/inscriure")
    public String inscriureFormacio(@RequestParam("idActividad") int idActividad, HttpSession session) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        activitatFormacioDao.inscriureAsistente(idActividad, asistente.getDni());
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