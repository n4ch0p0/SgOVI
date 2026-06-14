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
        String sql = "SELECT r.id_contracte, r.id_request, r.fecha_inici, r.fecha_fin, r.pdf_path, "
                + "u.dni AS dni_usuario, u.nom AS nom_usuari, u.cognoms AS cognoms_usuari, "
                + "req.tipusservei, req.preferencies "
                + "FROM RegistreContracte r "
                + "JOIN APRequest req ON r.id_request = req.id_request "
                + "JOIN UsuarioOVI u ON req.id_usuario = u.id_usuario "
                + "JOIN AssistentPersonal ap ON r.id_ap = ap.id_ap "
                + "WHERE ap.dni = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs), dniPap);
    }

    @Override
    public RegistreContracteAsistente getContracte(int id) {
        String sql = "SELECT r.id_contracte, r.id_request, r.fecha_inici, r.fecha_fin, r.pdf_path, "
                + "u.dni AS dni_usuario, u.nom AS nom_usuari, u.cognoms AS cognoms_usuari, "
                + "req.tipusservei, req.preferencies "
                + "FROM RegistreContracte r "
                + "JOIN APRequest req ON r.id_request = req.id_request "
                + "JOIN UsuarioOVI u ON req.id_usuario = u.id_usuario "
                + "WHERE r.id_contracte = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateEstatContracte(int id, String estat) {
        jdbcTemplate.update("UPDATE RegistreContracte SET estat = ? WHERE id_contracte = ?", estat, id);
    }

    @Override
    public boolean teContracteActiu(String dniAp) {
        String sql = "SELECT COUNT(*) FROM RegistreContracte r "
                + "JOIN AssistentPersonal ap ON r.id_ap = ap.id_ap "
                + "WHERE ap.dni = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dniAp);
        return count != null && count > 0;
    }

    private RegistreContracteAsistente mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        RegistreContracteAsistente c = new RegistreContracteAsistente();
        c.setIdSeleccion(rs.getInt("id_contracte"));
        c.setIdRequest(rs.getInt("id_request"));
        c.setDniUsuario(rs.getString("dni_usuario"));
        c.setNomUsuari(rs.getString("nom_usuari") + " " + rs.getString("cognoms_usuari"));
        c.setTipusServei(rs.getString("tipusservei"));
        c.setPreferencies(rs.getString("preferencies"));
        c.setEstat("Formalitzat");
        if (rs.getDate("fecha_inici") != null)
            c.setFechaInici(rs.getDate("fecha_inici").toLocalDate());
        if (rs.getDate("fecha_fin") != null)
            c.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        c.setPdfPath(rs.getString("pdf_path"));
        return c;
    }
}