package br.com.dataset.spotbugs;

import java.util.Date;

public class PerfilUsuario {
    private String nome;
    private Date dataCriacao; 

    public PerfilUsuario(String nome, Date dataCriacao) {
        this.nome = nome;
        this.dataCriacao = dataCriacao; 
    }

    public Date getDataCriacao() {
        return this.dataCriacao; 
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }
}