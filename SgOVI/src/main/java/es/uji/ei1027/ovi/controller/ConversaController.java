package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.dao.ConversaDao;
import es.uji.ei1027.ovi.dao.MissatgeDao;
import es.uji.ei1027.ovi.model.AssistentPersonal;
import es.uji.ei1027.ovi.model.Conversa;
import es.uji.ei1027.ovi.model.Missatge;
import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/conversa")
public class ConversaController {

    @Autowired
    private ConversaDao conversaDao;

    @Autowired
    private MissatgeDao missatgeDao;

    @GetMapping("/list")
    public String llistarConverses(HttpSession session, Model model) {
        UsuarioOVI usuario = (UsuarioOVI) session.getAttribute("usuarioLogueado");
        AssistentPersonal ap = (AssistentPersonal) session.getAttribute("asistenteLogueado");

        if (usuario != null) {
            model.addAttribute("converses", conversaDao.getConversesByUsuario(usuario.getDni()));
            model.addAttribute("perfil", "Usuari");
            return "usuario/list_comunicacions";
        } else if (ap != null) {
            model.addAttribute("converses", conversaDao.getConversesByAp(ap.getDni()));
            model.addAttribute("perfil", "AP");
            return "asistente/list_comunicacions";
        }
        return "redirect:/login";
    }

    @PostMapping("/add")
    public String iniciarConversa(@RequestParam("idRequest") int idRequest,
                                  @RequestParam("dniAp") String dniAp,
                                  HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/login";

        // Comprovem si la conversa ja existix abans de crear-la
        if (!conversaDao.existeixConversa(idRequest, dniAp)) {
            conversaDao.addConversaDni(idRequest, dniAp);
        }

        // Tant si l'acaba de crear com si ja existia, et porta al xat
        return "redirect:/conversa/list";
    }

    @PostMapping("/enviar")
    public String enviarMissatge(@RequestParam("idConversa") int idConversa,
                                 @RequestParam("mensaje") String texto,
                                 HttpSession session) {
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
        return "redirect:/conversa/list";
    }
}