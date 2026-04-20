package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.AssistentPersonal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class AsistenteDaoImpl implements AsistenteDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addAsistente(AssistentPersonal a) {
        String sql = "INSERT INTO AssistentPersonal (dni, nom, cognoms, email, telefono, contrasenya, tipus, formacioAcademica, experienciaPrevia, proximitatGeografica, actiu, estat, motiu_rebuig) " +
                "VALUES(?, ?, ?, ?, ?, ?, (?)::tipus_ap, ?, ?, ?, ?, 'Pendent'::estat_validacio, NULL)";

        jdbcTemplate.update(sql,
                a.getDni(), a.getNom(), a.getCognoms(), a.getEmail(), a.getTelefono(), a.getContrasenya(),
                a.getTipus(), a.getFormacioAcademica(), a.getExperienciaPrevia(), a.getProximitatGeografica(), true
        );
    }

    @Override
    public void updateAsistente(AssistentPersonal a) {
        String sql = "UPDATE AssistentPersonal SET formacioAcademica = ?, experienciaPrevia = ?, proximitatGeografica = ?, actiu = ? WHERE dni = ?";
        jdbcTemplate.update(sql, a.getFormacioAcademica(), a.getExperienciaPrevia(), a.getProximitatGeografica(), a.isActiu(), a.getDni());
    }

    // Aquest mètode ja no fa falta amb els nous requisits, però si la interfície t'ho demana, deixa'l buit o adapta'l
    @Override
    public void updateEstados(String dni, boolean estadoAceptado, boolean actiu, boolean estatAcceptat) {
    }

    @Override
    public AssistentPersonal getAsistente(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM AssistentPersonal WHERE dni = ?",
                    (rs, rowNum) -> mapejarAsistente(rs), dni);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<AssistentPersonal> getAsistentes() {
        return jdbcTemplate.query("SELECT * FROM AssistentPersonal", (rs, rowNum) -> mapejarAsistente(rs));
    }

    @Override
    public List<AssistentPersonal> getAsistentesByTipus(String tipus) {
        return jdbcTemplate.query("SELECT * FROM AssistentPersonal WHERE tipus=(?)::tipus_ap",
                (rs, rowNum) -> mapejarAsistente(rs), tipus);
    }

    @Override
    public List<AssistentPersonal> getAsistentesPendientes() {
        return jdbcTemplate.query("SELECT * FROM AssistentPersonal WHERE estat = 'Pendent'",
                (rs, rowNum) -> mapejarAsistente(rs));
    }

    @Override
    public void updateEstadoAsistente(String dni, boolean aceptado) {
        // Canviat per suportar l'string de l'enumerat i el rebuig
        String estat = aceptado ? "Acceptat" : "Rebutjat";
        jdbcTemplate.update("UPDATE AssistentPersonal SET estat = (?)::estat_validacio WHERE dni = ?", estat, dni);
    }

    @Override
    public List<AssistentPersonal> getCandidatosAdecuados(String tipus) {
        String sql = "SELECT * FROM AssistentPersonal WHERE actiu = true AND estat = 'Acceptat' AND tipus = (?)::tipus_ap";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapejarAsistente(rs), tipus);
    }

    // Mètode privat per no repetir el mapping
    private AssistentPersonal mapejarAsistente(java.sql.ResultSet rs) throws java.sql.SQLException {
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