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
    public List<RegistreContracteAsistente> getContractesByAsistente(String dniPap) {
        String sql = "SELECT r.id_contracte, u.dni AS dni_usuario, r.fecha_inici, r.fecha_fin, r.pdf_path " +
                "FROM RegistreContracte r " +
                "JOIN APRequest req ON r.id_request = req.id_request " +
                "JOIN UsuarioOVI u ON req.id_usuario = u.id_usuario " +
                "JOIN AssistentPersonal ap ON r.id_ap = ap.id_ap " +
                "WHERE ap.dni = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RegistreContracteAsistente c = new RegistreContracteAsistente();
            c.setIdSeleccion(rs.getInt("id_contracte"));
            c.setDniUsuario(rs.getString("dni_usuario"));
            if (rs.getDate("fecha_inici") != null) c.setFechaInici(rs.getDate("fecha_inici").toLocalDate());
            if (rs.getDate("fecha_fin") != null) c.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
            c.setPdfPath(rs.getString("pdf_path"));
            return c;
        }, dniPap);
    }
}