package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.ConversaTecnic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class ConversaTecnicDaoImpl implements ConversaTecnicDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MissatgeTecnicDao missatgeTecnicDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addConversa(String dniUsuario) {
        jdbcTemplate.update("INSERT INTO ConversaTecnic (dni_usuario) VALUES (?)", dniUsuario);
    }

    @Override
    public List<ConversaTecnic> getConversesByUsuario(String dniUsuario) {
        String sql = "SELECT ct.*, u.nom, u.cognoms FROM ConversaTecnic ct " +
                "JOIN UsuarioOVI u ON ct.dni_usuario = u.dni " +
                "WHERE ct.dni_usuario = ? ORDER BY ct.data_inici DESC";

        List<ConversaTecnic> converses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ConversaTecnic c = mapejarConversa(rs);
            c.setNomUsuario(rs.getString("nom") + " " + rs.getString("cognoms"));
            return c;
        }, dniUsuario);

        for (ConversaTecnic c : converses) {
            c.setMissatges(missatgeTecnicDao.getMissatgesByConversa(c.getIdConversaTecnic()));
        }
        return converses;
    }

    @Override
    public List<ConversaTecnic> getAllConverses() {
        String sql = "SELECT ct.*, u.nom, u.cognoms FROM ConversaTecnic ct " +
                "JOIN UsuarioOVI u ON ct.dni_usuario = u.dni " +
                "ORDER BY ct.data_inici DESC";

        List<ConversaTecnic> converses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ConversaTecnic c = mapejarConversa(rs);
            c.setNomUsuario(rs.getString("nom") + " " + rs.getString("cognoms"));
            return c;
        });

        for (ConversaTecnic c : converses) {
            c.setMissatges(missatgeTecnicDao.getMissatgesByConversa(c.getIdConversaTecnic()));
        }
        return converses;
    }

    @Override
    public boolean existeixConversa(String dniUsuario) {
        String sql = "SELECT COUNT(*) FROM ConversaTecnic WHERE dni_usuario = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dniUsuario);
        return count != null && count > 0;
    }

    private ConversaTecnic mapejarConversa(java.sql.ResultSet rs) throws java.sql.SQLException {
        ConversaTecnic c = new ConversaTecnic();
        c.setIdConversaTecnic(rs.getInt("id_conversa_tecnic"));
        c.setDniUsuario(rs.getString("dni_usuario"));
        if (rs.getTimestamp("data_inici") != null) {
            c.setDataInici(rs.getTimestamp("data_inici").toLocalDateTime());
        }
        return c;
    }
}
