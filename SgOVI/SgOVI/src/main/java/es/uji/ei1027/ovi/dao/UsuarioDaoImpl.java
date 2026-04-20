package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addUsuario(UsuarioOVI u) {
        jdbcTemplate.update("INSERT INTO UsuarioOVI (dni, nom, cognoms, email, telefono, contrasenya, consentimentInformat, estat, motiu_rebuig) VALUES(?, ?, ?, ?, ?, ?, ?, 'Pendent'::estat_validacio, NULL)",
                u.getDni(), u.getNom(), u.getCognoms(), u.getEmail(), u.getTelefono(), u.getContrasenya(), u.getConsentimentInformat());
    }

    @Override
    public void updateUsuario(UsuarioOVI u) {
        jdbcTemplate.update("UPDATE UsuarioOVI SET nom=?, cognoms=?, email=?, telefono=?, contrasenya=?, consentimentInformat=? WHERE dni=?",
                u.getNom(), u.getCognoms(), u.getEmail(), u.getTelefono(), u.getContrasenya(), u.getConsentimentInformat(), u.getDni());
    }

    @Override
    public UsuarioOVI getUsuario(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM UsuarioOVI WHERE dni=?",
                    (rs, rowNum) -> {
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
                    }, dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<UsuarioOVI> getUsuarios() {
        return jdbcTemplate.query("SELECT * FROM UsuarioOVI",
                (rs, rowNum) -> {
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
                });
    }
}