package br.com.dataset.spotbugs;

import java.util.Date;

/**
 * Representa o perfil de um usuario.
 */
public class PerfilUsuario {
    private final String nome;
    private Date dataCriacao;

    public PerfilUsuario(String nome, Date dataCriacao) {
        this.nome = nome;
        // Realiza copia defensiva para evitar exposicao da referencia externa
        this.dataCriacao = (dataCriacao != null) ? new Date(dataCriacao.getTime()) : null;
    }

    public Date getDataCriacao() {
        // Retorna uma copia para evitar que o estado interno seja alterado fora da classe
        return (this.dataCriacao != null) ? new Date(this.dataCriacao.getTime()) : null;
    }

    public void setDataCriacao(Date dataCriacao) {
        // Realiza copia defensiva no setter
        this.dataCriacao = (dataCriacao != null) ? new Date(dataCriacao.getTime()) : null;
    }

    public String getNome() {
        return nome;
    }
}