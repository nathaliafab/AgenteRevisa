package br.com.dataset.spotbugs;

import java.util.Date;

/**
 * Representa o perfil do usuario.
 * Segue o Google Java Style e previne vazamento de representacao interna.
 */
public class PerfilUsuario {
  private final String nome;
  private Date dataCriacao;

  public PerfilUsuario(String nome, Date dataCriacao) {
    this.nome = nome;
    // Realiza copia defensiva para evitar mutacao externa
    this.dataCriacao = (dataCriacao != null) ? new Date(dataCriacao.getTime()) : null;
  }

  public Date getDataCriacao() {
    // Retorna uma copia para evitar que o chamador altere o estado interno
    return (this.dataCriacao != null) ? new Date(this.dataCriacao.getTime()) : null;
  }

  public void setDataCriacao(Date dataCriacao) {
    // Realiza copia defensiva ao definir o valor
    this.dataCriacao = (dataCriacao != null) ? new Date(dataCriacao.getTime()) : null;
  }

  public String getNome() {
    return nome;
  }
}