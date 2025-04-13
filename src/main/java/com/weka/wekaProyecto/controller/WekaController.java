package com.weka.wekaProyecto.controller;
import org.springframework.http.ResponseEntity;


import com.weka.wekaProyecto.dto.RespuestaDiagnosticoDTO;
import com.weka.wekaProyecto.entities.Persona;
import com.weka.wekaProyecto.service.RecomendacionService;
import com.weka.wekaProyecto.service.WekaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/persona")
@CrossOrigin(origins = "http://localhost:4200")
public class WekaController {

    @Autowired
    private WekaService wekaService;

    @Autowired
    private RecomendacionService recomendacionService;

    // Lista para guardar resultados de predicciones
    private final List<ResultadoPrediccion> historial = new ArrayList<>();

    @PostMapping("/predecir")
    public RespuestaDiagnosticoDTO predecir(@RequestBody Persona persona) {
        String resultado = wekaService.predecirEnfermedad(persona);
        String recomendacion;
        if ("tested_positive".equals(resultado)) {
            recomendacion = recomendacionService.generarRecomendacion(persona);
        } else {
            recomendacion = "🎉 ¡Felicidades! No se detectan signos de diabetes. Sigue cuidando tu salud. 💪";
        }
        System.out.println("RECOMENDACIÓN GENERADA: " + recomendacion);


        ResultadoPrediccion prediccion = new ResultadoPrediccion(persona, resultado);
        historial.add(prediccion);

        return new RespuestaDiagnosticoDTO(resultado, recomendacion);
    }

    @GetMapping("/predecir")
    public ResultadoPrediccion predecirGet(
            @RequestParam double pregnancies,
            @RequestParam double plasmaGlucose,
            @RequestParam double bloodPressure,
            @RequestParam double skinThickness,
            @RequestParam double insulin,
            @RequestParam double bmi,
            @RequestParam double diabetesPedigreeFunction,
            @RequestParam double age) {

        Persona persona = new Persona();
        persona.setPregnancies(pregnancies);
        persona.setPlasmaGlucose(plasmaGlucose);
        persona.setBloodPressure(bloodPressure);
        persona.setSkinThickness(skinThickness);
        persona.setInsulin(insulin);
        persona.setBmi(bmi);
        persona.setDiabetesPedigreeFunction(diabetesPedigreeFunction);
        persona.setAge(age);

        String resultado = wekaService.predecirEnfermedad(persona);
        ResultadoPrediccion prediccion = new ResultadoPrediccion(persona, resultado);
        historial.add(prediccion);
        return prediccion;
    }

    // Historial de predicciones
    @GetMapping("/historial")
    public List<ResultadoPrediccion> obtenerHistorial() {
        return historial;
    }


    // Clase interna para el historial
    public static class ResultadoPrediccion {
        private Persona persona;
        private String resultado;

        public ResultadoPrediccion() {
        }

        public ResultadoPrediccion(Persona persona, String resultado) {
            this.persona = persona;
            this.resultado = resultado;
        }

        public Persona getPersona() {
            return persona;
        }

        public void setPersona(Persona persona) {
            this.persona = persona;
        }

        public String getResultado() {
            return resultado;
        }

        public void setResultado(String resultado) {
            this.resultado = resultado;
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarHistorial() {
        historial.clear(); // <- Esto limpia la lista
        return ResponseEntity.ok("Historial eliminado correctamente.");
    }

}