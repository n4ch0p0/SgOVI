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
        jdbcTemplate.update("INSERT INTO UsuarioOVI (dni, nom, cognoms, contrasenya, email, telefono, consentimentInformat) VALUES(?, ?, ?, ?, ?, ?, ?)",
                u.getDni(), u.getNom(), u.getCognoms(), u.getContrasenya(), u.getEmail(), u.getTelefono(), u.getConsentimentInformat());
    }

    @Override
    public void updateUsuario(UsuarioOVI u) {
        jdbcTemplate.update("UPDATE UsuarioOVI SET nom=?, cognoms=?, contrasenya=?, email=?, telefono=?, consentimentInformat=? WHERE dni=?",
                u.getNom(), u.getCognoms(), u.getContrasenya(), u.getEmail(), u.getTelefono(), u.getConsentimentInformat(), u.getDni());
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
                        u.setContrasenya(rs.getString("contrasenya"));
                        u.setEmail(rs.getString("email"));
                        u.setTelefono(rs.getInt("telefono"));
                        u.setConsentimentInformat(rs.getBoolean("consentimentInformat"));
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
                    u.setContrasenya(rs.getString("contrasenya"));
                    u.setEmail(rs.getString("email"));
                    u.setTelefono(rs.getInt("telefono"));
                    u.setConsentimentInformat(rs.getBoolean("consentimentInformat"));
                    return u;
                });
    }

}