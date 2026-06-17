import br.com.dataset.spotbugs.HistoricoAcessosDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PRCodeTest {

    private HistoricoAcessosDAO dao;
    private static final String URL = "jdbc:h2:mem:testdb";

    @BeforeAll
    public static void init() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @BeforeEach
    public void setup() throws SQLException {
        dao = new HistoricoAcessosDAO();
        try (Connection conn = DriverManager.getConnection(URL, "sa", "");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS LOGS");
            stmt.execute("CREATE TABLE LOGS (USUARIO VARCHAR(255), ACAO VARCHAR(255))");
        }
    }

    @Test
    public void testSalvarLogComSucesso() throws SQLException {
        String usuario = "testeUser";
        String acao = "LOGIN";

        dao.salvarLog(usuario, acao);

        try (Connection conn = DriverManager.getConnection(URL, "sa", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM LOGS WHERE USUARIO = 'testeUser'")) {
            
            assertTrue(rs.next(), "O registro deveria ter sido inserido no banco.");
            assertEquals(usuario, rs.getString("USUARIO"));
            assertEquals(acao, rs.getString("ACAO"));
        }
    }

    @Test
    public void testSalvarLogComDadosNulos() {
        assertThrows(SQLException.class, () -> {
            dao.salvarLog(null, null);
        });
    }
}