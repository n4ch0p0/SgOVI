package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ComunicacioUsuarioViPAP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class ComunicacioDaoImpl implements ComunicacioDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addMensaje(ComunicacioUsuarioViPAP c) {
        // Ajustado a las columnas reales de tu tabla: id_seleccion, mensaje, fecha
        jdbcTemplate.update("INSERT INTO comunicaciousuariovipap (id_seleccion, mensaje, fecha) VALUES(?, ?, ?)",
                c.getIdSeleccion(), c.getMensaje(), c.getFecha());
    }

    @Override
    public List<ComunicacioUsuarioViPAP> getMensajesPorUsuario(String dniUsuario) {
        // Este SQL hace el camino: Usuario -> Request -> Selección -> Mensajes
        String sql = "SELECT m.id_comunicacio, m.id_seleccion, m.mensaje, m.fecha " +
                "FROM comunicaciousuariovipap m " +
                "JOIN seleccion s ON m.id_seleccion = s.id_seleccion " +
                "JOIN aprequest r ON s.id_request = r.id_request " +
                "JOIN usuarioovi u ON r.id_usuario = u.id_usuario " +
                "WHERE u.dni = ? " +
                "ORDER BY m.fecha DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ComunicacioUsuarioViPAP c = new ComunicacioUsuarioViPAP();
            c.setId(rs.getInt("id_comunicacio"));
            c.setIdSeleccion(rs.getInt("id_seleccion"));
            c.setMensaje(rs.getString("mensaje"));
            if (rs.getDate("fecha") != null) {
                c.setFecha(rs.getDate("fecha").toLocalDate());
            }
            return c;
        }, dniUsuario);
    }
}