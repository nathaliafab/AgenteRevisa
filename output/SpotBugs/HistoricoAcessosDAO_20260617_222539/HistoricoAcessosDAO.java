package br.com.dataset.spotbugs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoricoAcessosDAO {

    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:h2:mem:testdb");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "sa");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("H2 Driver not found in classpath");
        }
    }

    public void salvarLog(String usuario, String acao) throws SQLException {
        String sql = "INSERT INTO LOGS (USUARIO, ACAO) VALUES (?, ?)";

        try (Connection conexao = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            stmt.setString(2, acao);
            stmt.executeUpdate();
        }
    }
}