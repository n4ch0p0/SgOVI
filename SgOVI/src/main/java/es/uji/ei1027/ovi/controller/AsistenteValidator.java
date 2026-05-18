package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AsistenteValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return AssistentPersonal.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        AssistentPersonal asistente = (AssistentPersonal) obj;

        if (asistente.getDni() == null || asistente.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "obligatori", "El DNI és obligatori");
        } else if (!asistente.getDni().matches("[0-9]{8}[A-Za-z]")) {
            errors.rejectValue("dni", "format", "El format del DNI no és correcte (8 dígits + lletra)");
        }

        if (asistente.getNom() == null || asistente.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "obligatori", "El nom és obligatori");
        }

        if (asistente.getCognoms() == null || asistente.getCognoms().trim().isEmpty()) {
            errors.rejectValue("cognoms", "obligatori", "Els cognoms són obligatoris");
        }

        if (asistente.getEmail() == null || asistente.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatori", "L'email és obligatori");
        }

        if (asistente.getContrasenya() == null || asistente.getContrasenya().trim().isEmpty()) {
            errors.rejectValue("contrasenya", "obligatori", "La contrasenya és obligatòria");
        }

        if (asistente.getTipus() == null || asistente.getTipus().trim().isEmpty()
                || "Selecciona el tipus".equals(asistente.getTipus())) {
            errors.rejectValue("tipus", "obligatori", "Cal seleccionar un tipus de servei");
        }
    }
}
