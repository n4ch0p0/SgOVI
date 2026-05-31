package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix l'usuari a la base de dades */
    @Override
    public void addUsuario(UsuarioOVI u) {
        String hashedPass = BCrypt.hashpw(u.getContrasenya(), BCrypt.gensalt());
        jdbcTemplate.update("INSERT INTO UsuarioOVI (dni, nom, cognoms, email, telefono, contrasenya, consentimentInformat, estat, motiu_rebuig) VALUES(?, ?, ?, ?, ?, ?, ?, 'Pendent'::estat_validacio, NULL)",
                u.getDni(), u.getNom(), u.getCognoms(), u.getEmail(), u.getTelefono(), hashedPass, u.getConsentimentInformat());
    }

    /* Actualitza els atributs de l'usuari (excepte el dni, que és la clau primària) */
    @Override
    public void updateUsuario(UsuarioOVI u) {
        String hashedPass = u.getContrasenya();
        // Només rehashejem si no està ja hashejat (BCrypt hashes start with $2a$ or $2b$ and are 60 chars long)
        if (hashedPass != null && !hashedPass.startsWith("$2a$") && !hashedPass.isEmpty()) {
            hashedPass = BCrypt.hashpw(u.getContrasenya(), BCrypt.gensalt());
        }
        jdbcTemplate.update("UPDATE UsuarioOVI SET nom=?, cognoms=?, email=?, telefono=?, contrasenya=?, consentimentInformat=? WHERE dni=?",
                u.getNom(), u.getCognoms(), u.getEmail(), u.getTelefono(), hashedPass, u.getConsentimentInformat(), u.getDni());
    }

    /* Obté l'usuari amb el dni donat. Torna null si no existeix. */
    @Override
    public UsuarioOVI getUsuario(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM UsuarioOVI WHERE dni=?",
                    new UsuarioOVIRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els usuaris. Torna una llista buida si no n'hi ha cap. */
    @Override
    public List<UsuarioOVI> getUsuarios() {
        try {
            return jdbcTemplate.query("SELECT * FROM UsuarioOVI",
                    new UsuarioOVIRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<UsuarioOVI>();
        }
    }

    @Override
    public Map<String, String> obtenerMapaNombresUsuarios() {
        return jdbcTemplate.query("SELECT dni, nom, cognoms FROM UsuarioOVI", rs -> {
            Map<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("dni"), rs.getString("nom") + " " + rs.getString("cognoms"));
            }
            return map;
        });
    }

    @Override
    public void anonimizarUsuario(String dni) {
        // Usem un hash BCrypt d'una UUID aleatòria perquè cap contrasenya
        // real coincidisca mai amb aquest valor (evita el re-login post-baixa)
        String invalidHash = BCrypt.hashpw(java.util.UUID.randomUUID().toString(), BCrypt.gensalt());
        String sql = "UPDATE UsuarioOVI SET nom='Usuari Eliminat', cognoms='', email='anonim@ovi.es', telefono=NULL, estat='Rebutjat'::estat_validacio, motiu_rebuig='Baixa sol·licitada per l''usuari', contrasenya=? WHERE dni=?";
        jdbcTemplate.update(sql, invalidHash, dni);
    }
}