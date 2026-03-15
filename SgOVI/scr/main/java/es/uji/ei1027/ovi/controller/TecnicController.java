package es.uji.ei1027.ovi.controller;


import es.uji.ei1027.ovi.service.OviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tecnic") // Todas las rutas empezarán por /tecnic
public class TecnicController {

    @GetMapping("/dashboard") // O la ruta que prefieras
    public String dashboard(Model model) {
        return "tecnic/dashboard"; // Debe existir el archivo templates/tecnic/dashboard.html
    }
}