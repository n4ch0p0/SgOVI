package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.RegistreContracteUsuarioOvi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class RegistreContracteUsuarioDaoImpl implements RegistreContracteUsuarioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<RegistreContracteUsuarioOvi> getContractesByUsuario(String dniUsuario) {
        String sql = "SELECT c.id_contracte, ap.dni AS dni_assistent, c.fecha_inici, c.fecha_fin, c.pdf_path " +
                "FROM RegistreContracte c " +
                "JOIN AssistentPersonal ap ON c.id_ap = ap.id_ap " +
                "JOIN APRequest r ON c.id_request = r.id_request " +
                "JOIN UsuarioOVI u ON r.id_usuario = u.id_usuario " +
                "WHERE u.dni = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RegistreContracteUsuarioOvi c = new RegistreContracteUsuarioOvi();
            c.setId(rs.getInt("id_contracte"));
            c.setDniAsistente(rs.getString("dni_assistent"));
            if (rs.getDate("fecha_inici") != null) c.setDataInici(rs.getDate("fecha_inici").toLocalDate());
            if (rs.getDate("fecha_fin") != null) c.setDataFi(rs.getDate("fecha_fin").toLocalDate());

            c.setEstat("Actiu");
            c.setPdfPath(rs.getString("pdf_path"));
            return c;
        }, dniUsuario);
    }

    @Override
    public void addContracte(int idRequest, int idAp, LocalDate fechaInici, LocalDate fechaFin, String pdfPath) {
        // 1. Inserim el contracte directament usant la request i l'assistent
        String sqlInsert = "INSERT INTO RegistreContracte (id_request, id_ap, fecha_inici, fecha_fin, pdf_path) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert, idRequest, idAp, fechaInici, fechaFin, pdfPath);

        // 2. Actualitzem l'estat de la petició perquè ja no aparega com "Aprovada" sinó tancada
        String sqlUpdate = "UPDATE APRequest SET estat = 'Tancada_Contracte'::estat_apr WHERE id_request = ?";
        jdbcTemplate.update(sqlUpdate, idRequest);
    }

    @Override
    public RegistreContracteUsuarioOvi getContracte(int id) {
        String sql = "SELECT c.id_contracte, ap.dni AS dni_assistent, c.fecha_inici, c.fecha_fin, c.pdf_path " +
                "FROM RegistreContracte c JOIN AssistentPersonal ap ON c.id_ap = ap.id_ap WHERE c.id_contracte = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            RegistreContracteUsuarioOvi c = new RegistreContracteUsuarioOvi();
            c.setId(rs.getInt("id_contracte"));
            c.setDniAsistente(rs.getString("dni_assistent"));
            c.setDataInici(rs.getDate("fecha_inici").toLocalDate());
            if (rs.getDate("fecha_fin") != null) c.setDataFi(rs.getDate("fecha_fin").toLocalDate());
            c.setPdfPath(rs.getString("pdf_path"));
            return c;
        }, id);
    }

    @Override
    public void updateContracte(int id, LocalDate fechaInici, LocalDate fechaFin) {
        String sql = "UPDATE RegistreContracte SET fecha_inici = ?, fecha_fin = ? WHERE id_contracte = ?";
        jdbcTemplate.update(sql, fechaInici, fechaFin, id);
    }

    @Override
    public List<RegistreContracteUsuarioOvi> getTodosLosContractes() {
        String sql = "SELECT c.id_contracte, u.dni AS dni_usuario, ap.dni AS dni_assistent, c.fecha_inici, c.fecha_fin " +
                "FROM RegistreContracte c " +
                "JOIN APRequest r ON c.id_request = r.id_request " +
                "JOIN UsuarioOVI u ON r.id_usuario = u.id_usuario " +
                "JOIN AssistentPersonal ap ON c.id_ap = ap.id_ap";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RegistreContracteUsuarioOvi c = new RegistreContracteUsuarioOvi();
            c.setId(rs.getInt("id_contracte"));

            // Unim el DNI de l'assistent i del client per a la vista del tècnic
            c.setDniAsistente(rs.getString("dni_assistent") + " (Client: " + rs.getString("dni_usuario") + ")");

            if (rs.getDate("fecha_inici") != null) c.setDataInici(rs.getDate("fecha_inici").toLocalDate());
            if (rs.getDate("fecha_fin") != null) c.setDataFi(rs.getDate("fecha_fin").toLocalDate());
            return c;
        });
    }
}