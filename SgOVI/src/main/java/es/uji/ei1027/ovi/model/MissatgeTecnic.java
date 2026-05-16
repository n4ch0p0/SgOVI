package es.uji.ei1027.ovi.model;

import java.time.LocalDateTime;

public class MissatgeTecnic {
    private int idMissatge;
    private int idConversaTecnic;
    private String emissor; // 'Usuari' o 'Tecnic'
    private String textMissatge;
    private LocalDateTime dataEnviament;

    public MissatgeTecnic() {}

    public int getIdMissatge() { return idMissatge; }
    public void setIdMissatge(int idMissatge) { this.idMissatge = idMissatge; }

    public int getIdConversaTecnic() { return idConversaTecnic; }
    public void setIdConversaTecnic(int idConversaTecnic) { this.idConversaTecnic = idConversaTecnic; }

    public String getEmissor() { return emissor; }
    public void setEmissor(String emissor) { this.emissor = emissor; }

    public String getTextMissatge() { return textMissatge; }
    public void setTextMissatge(String textMissatge) { this.textMissatge = textMissatge; }

    public LocalDateTime getDataEnviament() { return dataEnviament; }
    public void setDataEnviament(LocalDateTime dataEnviament) { this.dataEnviament = dataEnviament; }
}
