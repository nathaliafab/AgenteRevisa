package com.ficticio.pmd;

public enum StatusDoPedido {

    PENDENTE("PENDENTE"),
    PAGO("PAGO"),
    CANCELADO("CANCELADO"),
    ENVIADO("ENVIADO");

    private final String valor;

    StatusDoPedido(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}