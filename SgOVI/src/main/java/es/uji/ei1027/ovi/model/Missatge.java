package es.uji.ei1027.ovi.model;

import java.time.LocalDateTime;

public class Missatge {
    private int idMissatge;
    private int idConversa;
    private String emissor; // Serà 'Usuari' o 'AP' segons qui escriga
    private String textMissatge;
    private LocalDateTime dataEnviament;

    public Missatge() {}

    public int getIdMissatge() { return idMissatge; }
    public void setIdMissatge(int idMissatge) { this.idMissatge = idMissatge; }

    public int getIdConversa() { return idConversa; }
    public void setIdConversa(int idConversa) { this.idConversa = idConversa; }

    public String getEmissor() { return emissor; }
    public void setEmissor(String emissor) { this.emissor = emissor; }

    public String getTextMissatge() { return textMissatge; }
    public void setTextMissatge(String textMissatge) { this.textMissatge = textMissatge; }

    public LocalDateTime getDataEnviament() { return dataEnviament; }
    public void setDataEnviament(LocalDateTime dataEnviament) { this.dataEnviament = dataEnviament; }
}