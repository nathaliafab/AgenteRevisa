package com.ficticio.pmd;

public class ConfiguracoesGlobais {

    private final byte[] chavesSegurancaCriptografia;

    public ConfiguracoesGlobais() {
        this.chavesSegurancaCriptografia = new byte[] { 0x1A, 0x2B, 0x3C, 0x4D, 0x5E };
    }

    public byte[] getChavesSegurancaCriptografia() {
        return this.chavesSegurancaCriptografia;
    }

    public void imprimirChaves() {
        System.out.print("Chaves atuais: ");
        for (byte b : chavesSegurancaCriptografia) {
            System.out.printf("0x%02X ", b);
        }
        System.out.println();
    }
}
