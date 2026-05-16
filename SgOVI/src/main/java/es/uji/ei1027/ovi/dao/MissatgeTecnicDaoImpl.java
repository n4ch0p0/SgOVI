package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.MissatgeTecnic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MissatgeTecnicDaoImpl implements MissatgeTecnicDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addMissatge(MissatgeTecnic m) {
        jdbcTemplate.update(
                "INSERT INTO MissatgeTecnic (id_conversa_tecnic, emissor, text_missatge) VALUES (?, ?, ?)",
                m.getIdConversaTecnic(), m.getEmissor(), m.getTextMissatge());
    }

    @Override
    public List<MissatgeTecnic> getMissatgesByConversa(int idConversaTecnic) {
        return jdbcTemplate.query(
                "SELECT * FROM MissatgeTecnic WHERE id_conversa_tecnic = ? ORDER BY data_enviament ASC",
                (rs, rowNum) -> {
                    MissatgeTecnic m = new MissatgeTecnic();
                    m.setIdMissatge(rs.getInt("id_missatge"));
                    m.setIdConversaTecnic(rs.getInt("id_conversa_tecnic"));
                    m.setEmissor(rs.getString("emissor"));
                    m.setTextMissatge(rs.getString("text_missatge"));
                    if (rs.getTimestamp("data_enviament") != null) {
                        m.setDataEnviament(rs.getTimestamp("data_enviament").toLocalDateTime());
                    }
                    return m;
                }, idConversaTecnic);
    }
}
