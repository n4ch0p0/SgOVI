package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.UsuarioOVI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TecnicDaoImpl implements TecnicDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<UsuarioOVI> getUsuariosPendientes() {
        return jdbcTemplate.query("SELECT * FROM usuarioovi WHERE estat = 'Pendent'", (rs, rowNum) -> {
            UsuarioOVI u = new UsuarioOVI();
            u.setDni(rs.getString("dni"));
            u.setNom(rs.getString("nom"));
            return u;
        });
    }

    @Override
    public void actualizarEstadoUsuario(String dni, String nuevoEstado, String motivoRechazo) {
        jdbcTemplate.update("UPDATE usuarioovi SET estat = ?, motiu_rebuig = ? WHERE dni = ?",
                nuevoEstado, motivoRechazo, dni);
    }
}