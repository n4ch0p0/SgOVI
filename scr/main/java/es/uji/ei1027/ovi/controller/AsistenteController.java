package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.ActivitatFormacioDao;
import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.RegistreContracteUsuarioDao;
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
    private RegistreContracteUsuarioDao registreContracteUsuarioDao;

    @Autowired
    private RegistreContracteAsistenteDao registreContracteAsistenteDao;

    @Autowired
    private ActivitatFormacioDao activitatFormacioDao;


    // 1. EL DASHBOARD PRINCIPAL
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        model.addAttribute("asistente", asistente);
        return "asistente/dashboard";
    }

    // 2. EL BOTÓN "EL MEU PERFIL"
    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        model.addAttribute("asistente", asistente);
        return "asistente/perfil"; // Crearemos un archivo perfil.html
    }

    // 3. EL BOTON DE "GESTIONAR CONTRACTES"
    @GetMapping("/contractes")
    public String verContractes(HttpSession session, Model model) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        // Llamamos al DAO que acabamos de arreglar
        List<RegistreContracteAsistente> misContractes = registreContracteAsistenteDao.getContractesByPap(asistente.getDni());

        model.addAttribute("contractes", misContractes);
        return "asistente/contractes";
    }

    // 4. EL BOTÓN "ACTIVITATS DE FORMACIÓ"
    @GetMapping("/formacio")
    public String verFormacio(HttpSession session, Model model) {
        // 1. Comprobamos la sesión
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        // 2. Buscamos las actividades usando el método que ya tienes en tu DAO
        List<ActivitatFormacio> llistaActivitats = activitatFormacioDao.getActivitatsActives();

        // 3. Pasamos la lista a la vista
        model.addAttribute("activitats", llistaActivitats);

        return "asistente/formacio";
    }
    @PostMapping("/formacio/inscriure")
    public String inscriureFormacio(@RequestParam("idActividad") int idActividad, HttpSession session) {
        AssistentPersonal asistente = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistente == null) return "redirect:/login";

        // Llamamos al DAO usando el DNI del asistente de la sesión
        // (Asegúrate de que tu clase AssistentPersonal tiene el método getDni(). Si se llama getNif() o similar, cámbialo aquí)
        activitatFormacioDao.inscriureAsistente(idActividad, asistente.getDni());

        // Recargamos la página de formación
        return "redirect:/asistente/formacio";
    }
    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute("asistente") AssistentPersonal asistenteActualizado, HttpSession session) {
        AssistentPersonal asistenteSesion = (AssistentPersonal) session.getAttribute("asistenteLogueado");
        if (asistenteSesion == null) return "redirect:/login";

        // 1. Por seguridad, le asignamos el DNI y tipo que tiene en sesión (por si intentan hackear el formulario)
        asistenteActualizado.setDni(asistenteSesion.getDni());
        asistenteActualizado.setTipus(asistenteSesion.getTipus());
        asistenteActualizado.setEstadoAceptado(asistenteSesion.isEstadoAceptado());
        asistenteActualizado.setActiu(asistenteSesion.isActiu());

        // 2. Guardamos en la base de datos
        asistenteDao.updateAsistente(asistenteActualizado);

        // 3. Actualizamos los datos en la sesión para que el Dashboard muestre lo nuevo
        session.setAttribute("asistenteLogueado", asistenteActualizado);

        // 4. Volvemos al dashboard
        return "redirect:/asistente/dashboard";
    }



}