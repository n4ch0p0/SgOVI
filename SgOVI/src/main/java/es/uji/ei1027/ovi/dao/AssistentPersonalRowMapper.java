package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class AssistentPersonalRowMapper implements RowMapper<AssistentPersonal> {
    public AssistentPersonal mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssistentPersonal a = new AssistentPersonal();
        a.setDni(rs.getString("dni"));
        a.setNom(rs.getString("nom"));
        a.setCognoms(rs.getString("cognoms"));
        a.setEmail(rs.getString("email"));
        a.setTelefono(rs.getInt("telefono"));
        a.setContrasenya(rs.getString("contrasenya"));
        a.setTipus(rs.getString("tipus"));
        a.setFormacioAcademica(rs.getString("formacioAcademica"));
        a.setExperienciaPrevia(rs.getString("experienciaPrevia"));
        a.setProximitatGeografica(rs.getString("proximitatGeografica"));
        a.setActiu(rs.getBoolean("actiu"));
        a.setEstat(rs.getString("estat"));
        a.setMotiuRebuig(rs.getString("motiu_rebuig"));
        return a;
    }
}
