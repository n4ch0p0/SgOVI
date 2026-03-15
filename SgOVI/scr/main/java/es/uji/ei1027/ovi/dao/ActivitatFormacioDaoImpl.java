package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ActivitatFormacio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class ActivitatFormacioDaoImpl implements ActivitatFormacioDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addActivitat(ActivitatFormacio a) {
        jdbcTemplate.update("INSERT INTO ActivitatFormacio (dniFormador, titol, fecha, tipus) VALUES(?, ?, ?, ?)",
                a.getDniFormador(), a.getTitol(), a.getFecha(), a.getTipus());
    }

    @Override
    public List<ActivitatFormacio> getActivitatsActives() {
        // Si tu consulta original tenía un WHERE para filtrar activas, añádelo aquí
        String sql = "SELECT * FROM activitatformacio";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ActivitatFormacio a = new ActivitatFormacio();

            // Usamos los nombres EXACTOS de tu base de datos
            a.setId(rs.getInt("id_actividad"));

            // Tu BD devuelve un número (id_formador), pero tu Java espera un String (dniFormador)
            a.setDniFormador(String.valueOf(rs.getInt("id_formador")));

            a.setTitol(rs.getString("titol"));

            // Como la BD devuelve fecha y hora (Timestamp), la convertimos a LocalDate
            if (rs.getTimestamp("fecha") != null) {
                a.setFecha(rs.getTimestamp("fecha").toLocalDateTime().toLocalDate());
            }

            a.setTipus(rs.getString("tipus"));

            return a;
        });
    }
    @Override
    public void inscriureAsistente(int idActividad, String dniAsistente) {
        // Insertamos la actividad, buscamos el id_ap a partir del DNI, y ponemos assistencia a false por defecto
        String sql = "INSERT INTO assistenciaformacio (id_actividad, id_ap, assistencia) " +
                "VALUES (?, (SELECT id_ap FROM assistentpersonal WHERE dni = ?), false)";

        jdbcTemplate.update(sql, idActividad, dniAsistente);
    }
}