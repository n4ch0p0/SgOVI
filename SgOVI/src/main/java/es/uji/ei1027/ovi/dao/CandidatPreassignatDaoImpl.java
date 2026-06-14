package es.uji.ei1027.ovi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CandidatPreassignatDaoImpl implements CandidatPreassignatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveSeleccio(int idRequest, List<String> dniAps) {
        jdbcTemplate.update("DELETE FROM candidat_preassignat WHERE id_request = ?", idRequest);
        for (String dni : dniAps) {
            jdbcTemplate.update(
                "INSERT INTO candidat_preassignat (id_request, dni_ap) VALUES (?, ?)", idRequest, dni);
        }
    }

    @Override
    public List<String> getDniApsSeleccionats(int idRequest) {
        return jdbcTemplate.query(
            "SELECT dni_ap FROM candidat_preassignat WHERE id_request = ?",
            (rs, rowNum) -> rs.getString("dni_ap"),
            idRequest);
    }

    @Override
    public void removeAssistentFromAllSeleccions(String dniAp) {
        jdbcTemplate.update("DELETE FROM candidat_preassignat WHERE dni_ap = ?", dniAp);
    }
}
