package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.RegistreContracteAsistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class RegistreContracteAsistenteDaoImpl implements RegistreContracteAsistenteDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addContracte(RegistreContracteAsistente c) {
        jdbcTemplate.update("INSERT INTO RegistreContracte (idRequest, dniPap, fecha_Inici, fecha_Fin, pdf_Path) VALUES(?, ?, ?, ?, ?)",
                c.getIdRequest(), c.getDniPap(), c.getFecha_Inici(), c.getFecha_Fin(), c.getPdf_Path());
    }

    @Override
    public RegistreContracteAsistente getContracte(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM RegistreContracte WHERE id=?",
                (rs, rowNum) -> {
                    RegistreContracteAsistente c = new RegistreContracteAsistente();
                    c.setId(rs.getInt("id"));
                    c.setIdRequest(rs.getInt("idRequest"));
                    c.setDniPap(rs.getString("dniPap"));
                    // Asumiendo que usas java.sql.Date en la BD y lo pasas a LocalDate
                    if (rs.getDate("fecha_Inici") != null) c.setFecha_Inici(rs.getDate("fecha_Inici").toLocalDate());
                    if (rs.getDate("fecha_Fin") != null) c.setFecha_Fin(rs.getDate("fecha_Fin").toLocalDate());
                    c.setPdf_Path(rs.getString("pdf_Path"));
                    return c;
                }, id);
    }

    @Override
    public List<RegistreContracteAsistente> getContractesByPap(String dniPap) {
        // Cruzamos registrecontracte -> seleccion -> assistentpersonal para poder filtrar por el DNI de texto
        String sql = "SELECT r.id_contracte as id, r.id_seleccion as idRequest, ap.dni as dniPap, " +
                "r.fecha_inici, r.fecha_fin, r.pdf_path " +
                "FROM registrecontracte r " +
                "JOIN seleccion s ON r.id_seleccion = s.id_seleccion " +
                "JOIN assistentpersonal ap ON s.id_ap = ap.id_ap " +
                "WHERE ap.dni = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RegistreContracteAsistente c = new RegistreContracteAsistente();
            c.setId(rs.getInt("id"));
            c.setIdRequest(rs.getInt("idRequest"));
            c.setDniPap(rs.getString("dniPap"));
            if (rs.getDate("fecha_inici") != null) c.setFecha_Inici(rs.getDate("fecha_inici").toLocalDate());
            if (rs.getDate("fecha_fin") != null) c.setFecha_Fin(rs.getDate("fecha_fin").toLocalDate());
            c.setPdf_Path(rs.getString("pdf_path"));
            return c;
        }, dniPap);
    }

    @Override
    public List<RegistreContracteAsistente> getContractesByRequest(int idRequest) {
        // ... misma lógica del RowMapper pero con "WHERE idRequest=?"
        return null;
    }
}