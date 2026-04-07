package es.uji.ei1027.ovi.model;

public class AssistentPersonal {
    private String dni;
    private boolean estadoAceptado;
    private boolean actiu;
    private boolean estat_acceptat;
    private String formacioAcademica;
    private String experienciaPrevia;
    private String proximitatGeografica;
    private String tipus;

    public AssistentPersonal() {
    }


    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public boolean isEstadoAceptado() {
        return estadoAceptado;
    }

    public void setEstadoAceptado(boolean estadoAceptado) {
        this.estadoAceptado = estadoAceptado;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public boolean isEstat_acceptat() {
        return estat_acceptat;
    }

    public void setEstat_acceptat(boolean estat_acceptat) {
        this.estat_acceptat = estat_acceptat;
    }

    public String getFormacioAcademica() {
        return formacioAcademica;
    }

    public void setFormacioAcademica(String formacioAcademica) {
        this.formacioAcademica = formacioAcademica;
    }

    public String getExperienciaPrevia() {
        return experienciaPrevia;
    }

    public void setExperienciaPrevia(String experienciaPrevia) {
        this.experienciaPrevia = experienciaPrevia;
    }

    public String getProximitatGeografica() {
        return proximitatGeografica;
    }

    public void setProximitatGeografica(String proximitatGeografica) {
        this.proximitatGeografica = proximitatGeografica;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }
}
