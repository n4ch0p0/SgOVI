package es.uji.ei1027.ovi.dao;

import es.uji.ei1027.ovi.model.Missatge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MissatgeDaoImpl implements MissatgeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addMissatge(Missatge m) {
        jdbcTemplate.update("INSERT INTO Missatge (id_conversa, emissor, text_missatge) VALUES(?, (?)::tipus_emissor, ?)",
                m.getIdConversa(), m.getEmissor(), m.getTextMissatge());
    }

    @Override
    public List<Missatge> getMissatgesByConversa(int idConversa) {
        return jdbcTemplate.query("SELECT * FROM Missatge WHERE id_conversa = ? ORDER BY data_enviament ASC",
                (rs, rowNum) -> {
                    Missatge m = new Missatge();
                    m.setIdMissatge(rs.getInt("id_missatge"));
                    m.setIdConversa(rs.getInt("id_conversa"));
                    m.setEmissor(rs.getString("emissor"));
                    m.setTextMissatge(rs.getString("text_missatge"));
                    if (rs.getTimestamp("data_enviament") != null) {
                        m.setDataEnviament(rs.getTimestamp("data_enviament").toLocalDateTime());
                    }
                    return m;
                }, idConversa);
    }
}