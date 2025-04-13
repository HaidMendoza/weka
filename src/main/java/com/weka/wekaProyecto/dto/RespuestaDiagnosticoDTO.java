package com.weka.wekaProyecto.dto;

public class RespuestaDiagnosticoDTO {
    private String resultado;
    private String recomendacion;

    public RespuestaDiagnosticoDTO(String resultado, String recomendacion) {
        this.resultado = resultado;
        this.recomendacion = recomendacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }
}
