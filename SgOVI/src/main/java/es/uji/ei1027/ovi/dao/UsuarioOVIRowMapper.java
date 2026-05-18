package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class UsuarioOVIRowMapper implements RowMapper<UsuarioOVI> {
    public UsuarioOVI mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsuarioOVI u = new UsuarioOVI();
        u.setDni(rs.getString("dni"));
        u.setNom(rs.getString("nom"));
        u.setCognoms(rs.getString("cognoms"));
        u.setEmail(rs.getString("email"));
        u.setTelefono(rs.getInt("telefono"));
        u.setContrasenya(rs.getString("contrasenya"));
        u.setConsentimentInformat(rs.getBoolean("consentimentInformat"));
        u.setEstat(rs.getString("estat"));
        u.setMotiuRebuig(rs.getString("motiu_rebuig"));
        return u;
    }
}
