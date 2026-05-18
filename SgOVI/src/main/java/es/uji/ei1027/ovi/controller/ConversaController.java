package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.ConversaDao;
import es.uji.ei1027.ovi.dao.ConversaTecnicDao;
import es.uji.ei1027.ovi.dao.MissatgeDao;
import es.uji.ei1027.ovi.dao.MissatgeTecnicDao;
import es.uji.ei1027.ovi.dao.UsuarioDao;
import es.uji.ei1027.ovi.dao.AsistenteDao;
import es.uji.ei1027.ovi.dao.APRequestDao;
import es.uji.ei1027.ovi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/conversa")
public class ConversaController {

    @Autowired
    private ConversaDao conversaDao;

    @Autowired
    private MissatgeDao missatgeDao;

    @Autowired
    private ConversaTecnicDao conversaTecnicDao;

    @Autowired
    private MissatgeTecnicDao missatgeTecnicDao;

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private AsistenteDao asistenteDao;

    @Autowired
    private APRequestDao apRequestDao;

    // =============================================
    // CONVERSES USUARI <-> AP
    // =============================================

    @GetMapping("/list")
    public String llistarConverses(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("asistenteLogueado");

        if (usuario != null) {
            model.addAttribute("converses", conversaDao.getConversesByUsuario(usuario.getDni()));
            model.addAttribute("mapaNomsAssistents", asistenteDao.obtenerMapaNombresAsistentes());
            model.addAttribute("perfil", "Usuari");
            return "usuario/list_comunicacions";
        } else if (ap != null) {
            model.addAttribute("converses", conversaDao.getConversesByAp(ap.getDni()));
            model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
            model.addAttribute("mapaNomsRequests", apRequestDao.obtenerMapaNombresRequests());
            model.addAttribute("perfil", "AP");
            return "asistente/list_comunicacions";
        }
        return "redirect:/login";
    }

    @PostMapping("/add")
    public String iniciarConversa(@RequestParam("idRequest") int idRequest,
                                  @RequestParam("dniAp") String dniAp,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) { // <-- Parámetro añadido aquí
        if (session.getAttribute("usuarioLogueado") == null)
            return "redirect:/login";

        // Comprovem si la conversa ja existix abans de crear-la
        if (!conversaDao.existeixConversa(idRequest, dniAp)) {
            conversaDao.addConversaDni(idRequest, dniAp);
            redirectAttributes.addFlashAttribute("mensajeExito", "Xat de negociació iniciat correctament.");
        }

        // Tant si l'acaba de crear com si ja existia, et porta al xat
        return "redirect:/conversa/list";
    }

    @PostMapping("/enviar")
    public String enviarMissatge(@RequestParam("idConversa") int idConversa,
                                 @RequestParam("mensaje") String texto,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) { // <-- Parámetro añadido aquí
        Missatge m = new Missatge();
        m.setIdConversa(idConversa);
        m.setTextMissatge(texto);

        if (session.getAttribute("usuarioLogueado") != null) {
            m.setEmissor("Usuari");
        } else if (session.getAttribute("asistenteLogueado") != null) {
            m.setEmissor("AP");
        } else {
            return "redirect:/login";
        }

        missatgeDao.addMissatge(m);
        redirectAttributes.addFlashAttribute("mensajeExito", "Missatge enviat.");
        return "redirect:/conversa/list";
    }

    // =============================================
    // CONVERSES USUARI <-> TÈCNIC
    // =============================================

    @GetMapping("/tecnic/list")
    public String llistarConversesTecnic(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");

        if (session.getAttribute("tecnicLogueado") != null) {
            // Vista del tècnic: veu totes les converses
            model.addAttribute("converses", conversaTecnicDao.getAllConverses());
            model.addAttribute("mapaNomsUsuaris", usuarioDao.obtenerMapaNombresUsuarios());
            model.addAttribute("perfil", "Tecnic");
            return "tecnic/list_comunicacions";
        } else if (usuario != null) {
            // Vista de l'usuari: veu les seues converses amb el tècnic
            model.addAttribute("converses", conversaTecnicDao.getConversesByUsuario(usuario.getDni()));
            model.addAttribute("perfil", "Usuari");
            return "usuario/list_comunicacions_tecnic";
        }
        return "redirect:/login";
    }

    @PostMapping("/tecnic/iniciar")
    public String iniciarConversaTecnic(@RequestParam("dniUsuario") String dniUsuario,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) { // <-- Parámetro añadido aquí
        // Tant el tècnic com l'usuari poden iniciar la conversa
        if (session.getAttribute("tecnicLogueado") == null && session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        if (!conversaTecnicDao.existeixConversa(dniUsuario)) {
            conversaTecnicDao.addConversa(dniUsuario);
            redirectAttributes.addFlashAttribute("mensajeExito", "Conversa iniciada amb l'usuari.");
        }

        return "redirect:/conversa/tecnic/list";
    }

    @PostMapping("/tecnic/enviar")
    public String enviarMissatgeTecnic(@RequestParam("idConversaTecnic") int idConversaTecnic,
                                       @RequestParam("mensaje") String texto,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) { // <-- Parámetro añadido aquí
        MissatgeTecnic m = new MissatgeTecnic();
        m.setIdConversaTecnic(idConversaTecnic);
        m.setTextMissatge(texto);

        if (session.getAttribute("tecnicLogueado") != null) {
            m.setEmissor("Tecnic");
        } else if (session.getAttribute("usuarioLogueado") != null) {
            m.setEmissor("Usuari");
        } else {
            return "redirect:/login";
        }

        missatgeTecnicDao.addMissatge(m);
        redirectAttributes.addFlashAttribute("mensajeExito", "Missatge enviat.");
        return "redirect:/conversa/tecnic/list";
    }
}