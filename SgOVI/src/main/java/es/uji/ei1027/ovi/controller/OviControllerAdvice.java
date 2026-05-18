package es.uji.ei1027.ovi.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OviControllerAdvice {

    @ExceptionHandler(value = OviException.class)
    public String handleOviException(OviException ex, Model model) {
        model.addAttribute("errorName", ex.getErrorName());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorName", "Error inesperat");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
