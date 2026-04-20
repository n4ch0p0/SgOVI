package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.Conversa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class ConversaDaoImpl implements ConversaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MissatgeDao missatgeDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addConversa(int idRequest, int idAp) {
        jdbcTemplate.update("INSERT INTO Conversa (id_request, id_ap) VALUES(?, ?)", idRequest, idAp);
    }

    @Override
    public List<Conversa> getConversesByUsuario(String dniUsuario) {
        String sql = "SELECT c.*, ap.nom AS nom_ap, ap.cognoms AS cognoms_ap " +
                "FROM Conversa c " +
                "JOIN aprequest r ON c.id_request = r.id_request " +
                "JOIN usuarioovi u ON r.id_usuario = u.id_usuario " +
                "JOIN assistentpersonal ap ON c.id_ap = ap.id_ap " +
                "WHERE u.dni = ? ORDER BY c.data_inici DESC";

        List<Conversa> converses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Conversa c = mapejarConversa(rs);
            c.setNomAp(rs.getString("nom_ap") + " " + rs.getString("cognoms_ap"));
            return c;
        }, dniUsuario);

        // BUCLE: Rellenamos los mensajes de cada conversación
        for (Conversa c : converses) {
            c.setMissatges(missatgeDao.getMissatgesByConversa(c.getIdConversa()));
        }
        return converses;
    }

    @Override
    public List<Conversa> getConversesByAp(String dniAp) {
        String sql = "SELECT c.* FROM Conversa c JOIN assistentpersonal ap ON c.id_ap = ap.id_ap WHERE ap.dni = ? ORDER BY c.data_inici DESC";
        List<Conversa> converses = jdbcTemplate.query(sql, (rs, rowNum) -> mapejarConversa(rs), dniAp);

        // BUCLE: Rellenamos los mensajes de cada conversación
        for (Conversa c : converses) {
            c.setMissatges(missatgeDao.getMissatgesByConversa(c.getIdConversa()));
        }
        return converses;
    }

    private Conversa mapejarConversa(java.sql.ResultSet rs) throws java.sql.SQLException {
        Conversa c = new Conversa();
        c.setIdConversa(rs.getInt("id_conversa"));
        c.setIdRequest(rs.getInt("id_request"));
        c.setIdAp(rs.getInt("id_ap"));
        if (rs.getTimestamp("data_inici") != null) {
            c.setDataInici(rs.getTimestamp("data_inici").toLocalDateTime());
        }
        return c;
    }

    public void addConversaDni(int idRequest, String dniAp) {
        // La subconsulta (SELECT...) busca l'ID intern usant el DNI que li hem passat
        String sql = "INSERT INTO Conversa (id_request, id_ap) " +
                "VALUES (?, (SELECT id_ap FROM AssistentPersonal WHERE dni = ?))";

        jdbcTemplate.update(sql, idRequest, dniAp);
    }

    @Override
    public boolean existeixConversa(int idRequest, String dniAp) {
        String sql = "SELECT COUNT(*) FROM Conversa WHERE id_request = ? AND id_ap = (SELECT id_ap FROM AssistentPersonal WHERE dni = ?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idRequest, dniAp);
        return count != null && count > 0;
    }

    @Override
    public List<Conversa> getConversesByRequest(int idRequest) {
        String sql = "SELECT c.*, ap.nom AS nom_ap, ap.cognoms AS cognoms_ap " +
                "FROM Conversa c " +
                "JOIN assistentpersonal ap ON c.id_ap = ap.id_ap " +
                "WHERE c.id_request = ?";

        List<Conversa> converses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Conversa c = mapejarConversa(rs);
            c.setNomAp(rs.getString("nom_ap") + " " + rs.getString("cognoms_ap"));
            return c;
        }, idRequest);

        for (Conversa c : converses) {
            c.setMissatges(missatgeDao.getMissatgesByConversa(c.getIdConversa()));
        }
        return converses;
    }
}