package br.com.dataset.spotbugs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoAcessosDAO {

    public void salvarLog(String usuario, String acao) throws SQLException {
        String url = "jdbc:h2:mem:testdb";
    
        Connection conexao = DriverManager.getConnection(url, "sa", "");
        PreparedStatement stmt = conexao.prepareStatement("INSERT INTO LOGS (USUARIO, ACAO) VALUES (?, ?)");
        
        stmt.setString(1, usuario);
        stmt.setString(2, acao);
        stmt.executeUpdate();
        
        stmt.close();
        conexao.close();
    }
}