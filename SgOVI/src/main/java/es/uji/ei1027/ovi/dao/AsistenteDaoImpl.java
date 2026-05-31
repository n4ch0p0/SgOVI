package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.AssistentPersonal;
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
public class AsistenteDaoImpl implements AsistenteDao {

    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix l'assistent a la base de dades */
    @Override
    public void addAsistente(AssistentPersonal a) {
        String sql = "INSERT INTO AssistentPersonal (dni, nom, cognoms, email, telefono, contrasenya, tipus, formacioAcademica, experienciaPrevia, proximitatGeografica, actiu, estat, motiu_rebuig) " +
                "VALUES(?, ?, ?, ?, ?, ?, (?)::tipus_ap, ?, ?, ?, ?, 'Pendent'::estat_validacio, NULL)";
        
        String hashedPass = BCrypt.hashpw(a.getContrasenya(), BCrypt.gensalt());

        jdbcTemplate.update(sql,
                a.getDni(), a.getNom(), a.getCognoms(), a.getEmail(), a.getTelefono(), hashedPass,
                a.getTipus(), a.getFormacioAcademica(), a.getExperienciaPrevia(), a.getProximitatGeografica(), true
        );
    }

    /* Actualitza els atributs de l'assistent */
    @Override
    public void updateAsistente(AssistentPersonal a) {
        String sql = "UPDATE AssistentPersonal SET formacioAcademica = ?, experienciaPrevia = ?, proximitatGeografica = ?, actiu = ? WHERE dni = ?";
        jdbcTemplate.update(sql, a.getFormacioAcademica(), a.getExperienciaPrevia(), a.getProximitatGeografica(), a.isActiu(), a.getDni());
    }

    @Override
    public void updateEstados(String dni, boolean estadoAceptado, boolean actiu, boolean estatAcceptat) {
    }

    /* Obté l'assistent amb el dni donat. Torna null si no existeix. */
    @Override
    public AssistentPersonal getAsistente(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM AssistentPersonal WHERE dni = ?",
                    new AssistentPersonalRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els assistents. Torna una llista buida si no n'hi ha cap. */
    @Override
    public List<AssistentPersonal> getAsistentes() {
        try {
            return jdbcTemplate.query("SELECT * FROM AssistentPersonal",
                    new AssistentPersonalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistentPersonal>();
        }
    }

    @Override
    public List<AssistentPersonal> getAsistentesByTipus(String tipus) {
        try {
            return jdbcTemplate.query("SELECT * FROM AssistentPersonal WHERE tipus=(?)::tipus_ap",
                    new AssistentPersonalRowMapper(), tipus);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistentPersonal>();
        }
    }

    @Override
    public List<AssistentPersonal> getAsistentesPendientes() {
        try {
            return jdbcTemplate.query("SELECT * FROM AssistentPersonal WHERE estat = 'Pendent'",
                    new AssistentPersonalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistentPersonal>();
        }
    }

    @Override
    public void updateEstadoAsistente(String dni, boolean aceptado) {
        String estat = aceptado ? "Acceptat" : "Rebutjat";
        jdbcTemplate.update("UPDATE AssistentPersonal SET estat = (?)::estat_validacio WHERE dni = ?", estat, dni);
    }

    @Override
    public List<AssistentPersonal> getCandidatosAdecuados(String tipus, String preferencies) {
        // En un caso real haríamos un MATCH con TsVector en Postgres o usaríamos NLP.
        // Aquí hacemos un filtrado básico: ordenamos por si tienen alguna coincidencia en su perfil.
        String sql = "SELECT * FROM AssistentPersonal WHERE actiu = true AND estat = 'Acceptat' AND tipus = (?)::tipus_ap " +
                     "ORDER BY (CASE WHEN proximitatGeografica ILIKE ? THEN 1 WHEN formacioAcademica ILIKE ? THEN 2 ELSE 3 END) ASC";
                     
        String likeQuery = "%" + (preferencies != null ? preferencies.replace(" ", "%") : "") + "%";
        
        try {
            return jdbcTemplate.query(sql, new AssistentPersonalRowMapper(), tipus, likeQuery, likeQuery);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<AssistentPersonal>();
        }
    }

    @Override
    public Map<String, String> obtenerMapaNombresAsistentes() {
        return jdbcTemplate.query("SELECT dni, nom, cognoms FROM AssistentPersonal", rs -> {
            Map<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("dni"), rs.getString("nom") + " " + rs.getString("cognoms"));
            }
            return map;
        });
    }

    @Override
    public void anonimizarAsistente(String dni) {
        // Usem un hash BCrypt d'una UUID aleatòria perquè cap contrasenya
        // real coincidisca mai amb aquest valor (evita el re-login post-baixa)
        String invalidHash = BCrypt.hashpw(java.util.UUID.randomUUID().toString(), BCrypt.gensalt());
        String sql = "UPDATE AssistentPersonal SET nom='Assistent Eliminat', cognoms='', email='anonim@ovi.es', telefono=NULL, estat='Rebutjat'::estat_validacio, motiu_rebuig='Baixa sol·licitada', contrasenya=?, formacioAcademica='', experienciaPrevia='', proximitatGeografica='', actiu=FALSE WHERE dni=?";
        jdbcTemplate.update(sql, invalidHash, dni);
    }
}