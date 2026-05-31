package es.uji.ei1027.ovi.controller;

import es.uji.ei1027.ovi.model.APRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

@Component
public class APRequestValidator implements Validator {

    private static final Set<String> TIPUS_VALIDS = Set.of("PAP", "PATY");

    @Override
    public boolean supports(Class<?> cls) {
        return APRequest.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        APRequest req = (APRequest) obj;

        if (req.getTipusServei() == null || req.getTipusServei().trim().isEmpty()) {
            errors.rejectValue("tipusServei", "obligatori", "Cal seleccionar un tipus de servei");
        } else if (!TIPUS_VALIDS.contains(req.getTipusServei())) {
            errors.rejectValue("tipusServei", "invalid", "El tipus de servei no és vàlid");
        }

        if (req.getPreferencies() == null || req.getPreferencies().trim().isEmpty()) {
            errors.rejectValue("preferencies", "obligatori", "Les preferències són obligatòries");
        } else if (req.getPreferencies().trim().length() < 5) {
            errors.rejectValue("preferencies", "massa_curt",
                    "Les preferències han de tindre almenys 5 caràcters");
        } else if (req.getPreferencies().length() > 500) {
            errors.rejectValue("preferencies", "massa_llarg",
                    "Les preferències no poden superar els 500 caràcters");
        }
    }
}
