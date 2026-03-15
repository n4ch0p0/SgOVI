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
        // Fíjate en el cast '(?)::tipus_ap' para el quinto parámetro
        String sql = "INSERT INTO assistentpersonal (dni, formacioacademica, experienciaprevia, proximitatgeografica, tipus, estadoaceptado, actiu, estat_acceptat) " +
                "VALUES(?, ?, ?, ?, (?)::tipus_ap, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    a.getDni(),
                    a.getFormacioAcademica(),
                    a.getExperienciaPrevia(),
                    a.getProximitatGeografica(),
                    a.getTipus(), // Aquí Java enviará "PAP" o "PATY" y Postgres lo convertirá
                    false,
                    true,
                    false
            );
        } catch (Exception e) {
            System.err.println("ERROR FINAL: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateAsistente(AssistentPersonal a) {
        // Actualizamos solo los campos que dejamos editar en el formulario
        String sql = "UPDATE assistentpersonal SET formacioacademica = ?, experienciaprevia = ?, proximitatgeografica = ? WHERE dni = ?";

        jdbcTemplate.update(sql,
                a.getFormacioAcademica(),
                a.getExperienciaPrevia(),
                a.getProximitatGeografica(),
                a.getDni()
        );
    }

    @Override
    public void updateEstados(String dni, boolean estadoAceptado, boolean actiu, boolean estatAcceptat) {
        jdbcTemplate.update("UPDATE AssitentPersonal SET estadoAceptado=?, actiu=?, estat_acceptat=? WHERE dni=?",
                estadoAceptado, actiu, estatAcceptat, dni);
    }

    @Override
    public AssistentPersonal getAsistente(String dni) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM assistentpersonal WHERE dni = ?",
                    (rs, rowNum) -> {
                        AssistentPersonal a = new AssistentPersonal();
                        a.setDni(rs.getString("dni"));
                        a.setTipus(rs.getString("tipus"));
                        a.setFormacioAcademica(rs.getString("formacioacademica"));
                        a.setExperienciaPrevia(rs.getString("experienciaprevia"));
                        a.setProximitatGeografica(rs.getString("proximitatgeografica"));
                        return a;
                    }, dni);
        } catch (EmptyResultDataAccessException e) {
            return null; // Si no existe el DNI, devolvemos null para que el controlador siga buscando
        }
    }

    @Override
    public List<AssistentPersonal> getAsistentes() {
        return jdbcTemplate.query("SELECT * FROM AssitentPersonal",
                (rs, rowNum) -> {
                    AssistentPersonal a = new AssistentPersonal();
                    a.setDni(rs.getString("dni"));
                    a.setFormacioAcademica(rs.getString("formacioAcademica"));
                    a.setExperienciaPrevia(rs.getString("experienciaPrevia"));
                    a.setProximitatGeografica(rs.getString("proximitatGeografica"));
                    a.setTipus(rs.getString("tipus"));
                    a.setEstadoAceptado(rs.getBoolean("estadoAceptado"));
                    a.setActiu(rs.getBoolean("actiu"));
                    a.setEstat_acceptat(rs.getBoolean("estat_acceptat"));
                    return a;
                });
    }

    @Override
    public List<AssistentPersonal> getAsistentesByTipus(String tipus) {
        return jdbcTemplate.query("SELECT * FROM AssitentPersonal WHERE tipus=?",
                (rs, rowNum) -> {
                    AssistentPersonal a = new AssistentPersonal();
                    a.setDni(rs.getString("dni"));
                    a.setFormacioAcademica(rs.getString("formacioAcademica"));
                    a.setExperienciaPrevia(rs.getString("experienciaPrevia"));
                    a.setProximitatGeografica(rs.getString("proximitatGeografica"));
                    a.setTipus(rs.getString("tipus"));
                    a.setEstadoAceptado(rs.getBoolean("estadoAceptado"));
                    a.setActiu(rs.getBoolean("actiu"));
                    a.setEstat_acceptat(rs.getBoolean("estat_acceptat"));
                    return a;
                }, tipus);
    }

    @Override
    public List<AssistentPersonal> getAsistentesPendientes() {
        return jdbcTemplate.query("SELECT * FROM AssitentPersonal WHERE estadoAceptado = false OR estadoAceptado IS NULL",
                (rs, rowNum) -> {
                    AssistentPersonal a = new AssistentPersonal();
                    a.setDni(rs.getString("dni"));
                    a.setFormacioAcademica(rs.getString("formacioAcademica"));
                    a.setExperienciaPrevia(rs.getString("experienciaPrevia"));
                    a.setProximitatGeografica(rs.getString("proximitatGeografica"));
                    a.setTipus(rs.getString("tipus"));
                    a.setEstadoAceptado(rs.getBoolean("estadoAceptado"));
                    a.setActiu(rs.getBoolean("actiu"));
                    a.setEstat_acceptat(rs.getBoolean("estat_acceptat"));
                    return a;
                });
    }

    @Override
    public void updateEstadoAsistente(String dni, boolean aceptado) {
        jdbcTemplate.update("UPDATE AssitentPersonal SET estadoAceptado = ? WHERE dni = ?", aceptado, dni);
    }

}