package com.ficticio.pmd;

public class AvaliadorDeCredito {

    public double avaliarLimiteCredito(int score, boolean possuiDividas, boolean possuiRendaComprovada, String tipoCartao, int tempoEmpregoMeses) {
        double limiteFinal = 0.0;

        if (possuiDividas && !possuiRendaComprovada) {
            if (score < 300) {
                limiteFinal = 0.0;
            } else if (score >= 300 && score < 500) {
                limiteFinal = 200.0;
            } else {
                limiteFinal = 500.0;
            }
        } else {
            if (tipoCartao == null) {
                tipoCartao = "BASIC";
            }

            switch (tipoCartao.toUpperCase()) {
                case "BASIC":
                    if (score < 400) {
                        limiteFinal = 600.0;
                    } else if (score >= 400 && score < 700) {
                        if (tempoEmpregoMeses > 12) {
                            limiteFinal = 1200.0;
                        } else {
                            limiteFinal = 800.0;
                        }
                    } else {
                        limiteFinal = 2000.0;
                    }
                    break;

                case "GOLD":
                    if (score < 600) {
                        limiteFinal = 1500.0;
                    } else if (score >= 600 && score < 800) {
                        if (tempoEmpregoMeses > 24 || possuiRendaComprovada) {
                            limiteFinal = 3500.0;
                        } else {
                            limiteFinal = 2500.0;
                        }
                    } else {
                        limiteFinal = 5000.0;
                    }
                    break;

                case "PLATINUM":
                    if (score < 700) {
                        limiteFinal = 4000.0;
                    } else if (score >= 700 && score < 900) {
                        if (tempoEmpregoMeses > 36 && possuiRendaComprovada) {
                            limiteFinal = 8000.0;
                        } else {
                            limiteFinal = 6000.0;
                        }
                    } else {
                        limiteFinal = 12000.0;
                    }
                    break;

                case "BLACK":
                    if (score < 850) {
                        if (possuiRendaComprovada) {
                            limiteFinal = 15000.0;
                        } else {
                            limiteFinal = 10000.0;
                        }
                    } else {
                        if (tempoEmpregoMeses > 48 || score > 950) {
                            limiteFinal = 30000.0;
                        } else {
                            limiteFinal = 20000.0;
                        }
                    }
                    break;

                default:
                    limiteFinal = 500.0;
                    break;
            }
        }

        return limiteFinal;
    }
}
