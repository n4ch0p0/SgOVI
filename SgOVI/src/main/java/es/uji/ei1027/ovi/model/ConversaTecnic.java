package es.uji.ei1027.ovi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConversaTecnic {
    private int idConversaTecnic;
    private String dniUsuario;
    private LocalDateTime dataInici;
    private String nomUsuario;
    private List<MissatgeTecnic> missatges = new ArrayList<>();

    public ConversaTecnic() {}

    public int getIdConversaTecnic() { return idConversaTecnic; }
    public void setIdConversaTecnic(int idConversaTecnic) { this.idConversaTecnic = idConversaTecnic; }

    public String getDniUsuario() { return dniUsuario; }
    public void setDniUsuario(String dniUsuario) { this.dniUsuario = dniUsuario; }

    public LocalDateTime getDataInici() { return dataInici; }
    public void setDataInici(LocalDateTime dataInici) { this.dataInici = dataInici; }

    public String getNomUsuario() { return nomUsuario; }
    public void setNomUsuario(String nomUsuario) { this.nomUsuario = nomUsuario; }

    public List<MissatgeTecnic> getMissatges() { return missatges; }
    public void setMissatges(List<MissatgeTecnic> missatges) { this.missatges = missatges; }
}
