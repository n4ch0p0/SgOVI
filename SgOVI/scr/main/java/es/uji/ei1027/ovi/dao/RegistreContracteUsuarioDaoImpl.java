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
        // CORRECCIÓN: Pedimos ap.id_ap en lugar de ap.dni porque esa columna no existe
        String sql = "SELECT c.id_contracte, ap.id_ap AS id_assistent, c.fecha_inici, c.fecha_fin, c.pdf_path " +
                "FROM registrecontracte c " +
                "JOIN seleccion s ON c.id_seleccion = s.id_seleccion " +
                "JOIN assistentpersonal ap ON s.id_ap = ap.id_ap " +
                "JOIN aprequest r ON s.id_request = r.id_request " +
                "JOIN usuarioovi u ON r.id_usuario = u.id_usuario " +
                "WHERE u.dni = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RegistreContracteUsuarioOvi c = new RegistreContracteUsuarioOvi();
            c.setId(rs.getInt("id_contracte"));

            // Guardamos el ID numérico transformado a texto en nuestra variable dniAsistente
            c.setDniAsistente(String.valueOf(rs.getInt("id_assistent")));

            if (rs.getDate("fecha_inici") != null) {
                c.setDataInici(rs.getDate("fecha_inici").toLocalDate());
            }
            if (rs.getDate("fecha_fin") != null) {
                c.setDataFi(rs.getDate("fecha_fin").toLocalDate());
            }

            if (c.getDataFi() == null || !c.getDataFi().isBefore(LocalDate.now())) {
                c.setEstat("Actiu");
            } else {
                c.setEstat("Finalitzat");
            }

            c.setPdfPath(rs.getString("pdf_path"));
            return c;
        }, dniUsuario);
    }
}