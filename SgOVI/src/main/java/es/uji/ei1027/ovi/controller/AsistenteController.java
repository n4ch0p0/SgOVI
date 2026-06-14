package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import es.uji.ei1027.ovi.dao.ActivitatFormacioDao;
import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.ConversaDao;
import es.uji.ei1027.ovi.dao.RegistreContracteAsistenteDao;
import es.uji.ei1027.ovi.dao.UsuarioDao;
import es.uji.ei1027.ovi.dao.APRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;

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

    @Autowired
    private ConversaDao conversaDao;

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
    public String actualizarPerfil(@ModelAttribute("asistente") AssistentPersonal asistenteActualizado,
                                   BindingResult bindingResult, HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        AssistentPersonal asistenteSesion = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistenteSesion == null) return "redirect:/login";

        // Validació server-side dels camps editables
        if (asistenteActualizado.getFormacioAcademica() == null
                || asistenteActualizado.getFormacioAcademica().trim().isEmpty()) {
            bindingResult.rejectValue("formacioAcademica", "obligatori",
                    "La formació acadèmica és obligatòria");
        }
        if (asistenteActualizado.getExperienciaPrevia() == null
                || asistenteActualizado.getExperienciaPrevia().trim().isEmpty()) {
            bindingResult.rejectValue("experienciaPrevia", "obligatori",
                    "L'experiència prèvia és obligatòria");
        }
        if (asistenteActualizado.getProximitatGeografica() == null
                || asistenteActualizado.getProximitatGeografica().trim().isEmpty()) {
            bindingResult.rejectValue("proximitatGeografica", "obligatori",
                    "La proximitat geogràfica és obligatòria");
        }

        if (bindingResult.hasErrors()) {
            // Restaurem dades de sessió que no venen del formulari
            asistenteActualizado.setDni(asistenteSesion.getDni());
            asistenteActualizado.setTipus(asistenteSesion.getTipus());
            asistenteActualizado.setEstat(asistenteSesion.getEstat());
            return "asistente/perfil";
        }

        // Per seguretat, mantenim les dades crítiques que no s'editen al formulari
        asistenteActualizado.setDni(asistenteSesion.getDni());
        asistenteActualizado.setNom(asistenteSesion.getNom());
        asistenteActualizado.setCognoms(asistenteSesion.getCognoms());
        asistenteActualizado.setEmail(asistenteSesion.getEmail());
        asistenteActualizado.setTelefono(asistenteSesion.getTelefono());
        asistenteActualizado.setContrasenya(asistenteSesion.getContrasenya());
        asistenteActualizado.setTipus(asistenteSesion.getTipus());
        asistenteActualizado.setEstat(asistenteSesion.getEstat());
        asistenteActualizado.setMotiuRebuig(asistenteSesion.getMotiuRebuig());
        asistenteActualizado.setActiu(asistenteSesion.isActiu());

        asistenteDao.updateAsistente(asistenteActualizado);
        session.setAttribute("asistenteLogueado", asistenteActualizado);

        redirectAttributes.addFlashAttribute("mensajeExito", "El teu perfil s'ha actualitzat correctament.");
        return "redirect:/asistente/perfil";
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
        return "redirect:/conversa/list";
    }

    // 5. PROJECTE DE VIDA D'UN CLIENT (accessible una vegada hi ha conversa)
    @GetMapping("/client/projecte-vida")
    public String veureProjecteVidaClient(@RequestParam("dniUsuari") String dniUsuari,
                                          HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null || !"Acceptat".equals(asistente.getEstat())) return "redirect:/login";
        if (!conversaDao.existeixConversaEntreApIUsuari(asistente.getDni(), dniUsuari))
            return "redirect:/conversa/list";

        UsuarioOVI client = usuarioDao.getUsuario(dniUsuari);
        if (client == null) return "redirect:/conversa/list";

        model.addAttribute("nomUsuari", client.getNom() + " " + client.getCognoms());
        model.addAttribute("projecteVida", client.getProjecteVida());
        model.addAttribute("backUrl", "/conversa/list");
        return "usuario/projecte_vida_readonly";
    }

}