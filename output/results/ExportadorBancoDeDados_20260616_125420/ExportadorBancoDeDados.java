package com.ficticio.pmd;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportadorBancoDeDados {

  public void exportarBackupCsv(String url, String usuario, String senha, String caminhoArquivo) {
    String sql = "SELECT id, nome, email FROM usuarios";

    try (Connection conn = DriverManager.getConnection(url, usuario, senha);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        PrintWriter writer = new PrintWriter(new FileWriter(caminhoArquivo))) {

      writer.println("ID;Nome;Email");

      while (rs.next()) {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");

        writer.println(id + ";" + nome + ";" + email);
      }

    } catch (SQLException | IOException e) {
      System.out.println("Falha crítica no backup. Operação abortada. Motivo: " + e.getMessage());
    }
  }
}