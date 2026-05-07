package es.uji.ei1027.ovi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conversa {
    private int idConversa;
    private int idRequest;
    private int idAp;
    private LocalDateTime dataInici;
    private String nomAp;
    private List<Missatge> missatges = new ArrayList<>();

    public Conversa() {}

    public int getIdConversa() { return idConversa; }
    public void setIdConversa(int idConversa) { this.idConversa = idConversa; }

    public int getIdRequest() { return idRequest; }
    public void setIdRequest(int idRequest) { this.idRequest = idRequest; }

    public int getIdAp() { return idAp; }
    public void setIdAp(int idAp) { this.idAp = idAp; }

    public LocalDateTime getDataInici() { return dataInici; }
    public void setDataInici(LocalDateTime dataInici) { this.dataInici = dataInici; }

    public String getNomAp() { return nomAp; }
    public void setNomAp(String nomAp) { this.nomAp = nomAp; }

    public List<Missatge> getMissatges() { return missatges; }
    public void setMissatges(List<Missatge> missatges) { this.missatges = missatges; }
}