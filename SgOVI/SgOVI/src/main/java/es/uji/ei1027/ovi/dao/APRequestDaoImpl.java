package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.APRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class APRequestDaoImpl implements APRequestDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addRequest(APRequest r) {
        // Hacemos una subconsulta para sacar el id_usuario a partir del DNI,
        // y usamos ::tipus_ap y ::estat_apr para decirle a Postgres que son ENUMS
        String sql = "INSERT INTO aprequest (id_usuario, tipusservei, preferencies, estat) " +
                "VALUES ((SELECT id_usuario FROM usuarioovi WHERE dni = ?), ?::tipus_ap, ?, 'Revisio'::estat_apr)";

        jdbcTemplate.update(sql, r.getDniUsuario(), r.getTipusServei(), r.getPreferencies());
    }

    @Override
    public List<APRequest> getRequestsByUsuario(String dniUsuario) {
        // Juntamos aprequest con usuarioovi para recuperar el DNI y meterlo en nuestro objeto Java
        String sql = "SELECT a.*, u.dni as dni_usuari FROM aprequest a " +
                "JOIN usuarioovi u ON a.id_usuario = u.id_usuario " +
                "WHERE u.dni = ?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    APRequest r = new APRequest();
                    r.setId(rs.getInt("id_request"));
                    r.setDniUsuario(rs.getString("dni_usuari"));
                    r.setTipusServei(rs.getString("tipusservei"));
                    r.setPreferencies(rs.getString("preferencies"));
                    r.setEstat(rs.getString("estat"));
                    return r;
                }, dniUsuario);
    }

    @Override
    public List<APRequest> getRequestsPendientes() {
        String sql = "SELECT a.*, u.dni as dni_usuari FROM aprequest a " +
                "JOIN usuarioovi u ON a.id_usuario = u.id_usuario " +
                "WHERE a.estat = 'Revisio'";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    APRequest r = new APRequest();
                    r.setId(rs.getInt("id_request"));
                    r.setDniUsuario(rs.getString("dni_usuari"));
                    r.setTipusServei(rs.getString("tipusservei"));
                    r.setPreferencies(rs.getString("preferencies"));
                    r.setEstat(rs.getString("estat"));
                    return r;
                });
    }

    @Override
    public void updateEstado(int id, String estat) {
        jdbcTemplate.update("UPDATE aprequest SET estat = ?::estat_apr WHERE id_request = ?", estat, id);
    }

    @Override
    public APRequest getRequest(int idRequest) {
        String sql = "SELECT * FROM aprequest WHERE id_request = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            APRequest r = new APRequest();
            r.setId(rs.getInt("id_request"));
            r.setDniUsuario(rs.getString("id_usuario")); // Nota: ajusta si necesitas el DNI real
            r.setTipusServei(rs.getString("tipusservei"));
            r.setPreferencies(rs.getString("preferencies"));
            r.setEstat(rs.getString("estat"));
            return r;
        }, idRequest);
    }
}