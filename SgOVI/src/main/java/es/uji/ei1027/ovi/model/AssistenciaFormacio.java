package es.uji.ei1027.ovi.model;

public class AssistenciaFormacio {
    private int idActivitat;     // FK: ID de la ActivitatFormacio
    private String dniPap;       // FK: DNI del AssitentPersonal
    private boolean assistencia;
    private String url_certificat;

    public AssistenciaFormacio() {
    }

    public int getIdActivitat() {
        return idActivitat;
    }

    public void setIdActivitat(int idActivitat) {
        this.idActivitat = idActivitat;
    }

    public String getDniPap() {
        return dniPap;
    }

    public void setDniPap(String dniPap) {
        this.dniPap = dniPap;
    }

    public boolean isAssistencia() {
        return assistencia;
    }

    public void setAssistencia(boolean assistencia) {
        this.assistencia = assistencia;
    }

    public String getUrl_certificat() {
        return url_certificat;
    }

    public void setUrl_certificat(String url_certificat) {
        this.url_certificat = url_certificat;
    }
}

