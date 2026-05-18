package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UsuarioValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return UsuarioOVI.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UsuarioOVI usuario = (UsuarioOVI) obj;

        if (usuario.getDni() == null || usuario.getDni().trim().isEmpty()) {
            errors.rejectValue("dni", "obligatori", "El DNI és obligatori");
        } else if (!usuario.getDni().matches("[0-9]{8}[A-Za-z]")) {
            errors.rejectValue("dni", "format", "El format del DNI no és correcte (8 dígits + lletra)");
        }

        if (usuario.getNom() == null || usuario.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "obligatori", "El nom és obligatori");
        }

        if (usuario.getCognoms() == null || usuario.getCognoms().trim().isEmpty()) {
            errors.rejectValue("cognoms", "obligatori", "Els cognoms són obligatoris");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "obligatori", "L'email és obligatori");
        }

        if (usuario.getContrasenya() == null || usuario.getContrasenya().trim().isEmpty()) {
            errors.rejectValue("contrasenya", "obligatori", "La contrasenya és obligatòria");
        }

        if (usuario.getConsentimentInformat() == null || !usuario.getConsentimentInformat()) {
            errors.rejectValue("consentimentInformat", "obligatori",
                    "Cal acceptar el consentiment informat");
        }
    }
}
