package com.weka.wekaProyecto.service;
import com.weka.wekaProyecto.dto.RespuestaDiagnosticoDTO;


import com.weka.wekaProyecto.entities.Persona;
import java.util.List;

public interface WekaService {
    String predecirEnfermedad(Persona persona);
    List<Persona> obtenerHistorial();
    void eliminarHistorial();



}
