package com.weka.wekaProyecto.servicesIMP;

// Importación de la clase Persona que representa los datos de entrada
import com.weka.wekaProyecto.entities.Persona;
// Interfaz que esta clase implementa
import com.weka.wekaProyecto.service.WekaService;
import org.springframework.stereotype.Service;

// Librerías de WEKA para clasificación y manejo de instancias
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

// Anotación que indica que esta clase es un servicio de Spring (para inyección de dependencias)
@Service
public class WekaServiceImpl implements WekaService {

    // Lista que guarda el historial de predicciones realizadas, jesu aqui debe ir la base de datos
    private final List<Persona> historialResultados = new ArrayList<>();

    // Este metodo es que realiza la predeccion de la enfermedad con los atributos
    @Override
    public String predecirEnfermedad(Persona persona) {
        try {
            // jesu, con esto cargo el dataset para poder trabajr con sus datos (arff)
            InputStream arffStream = getClass().getClassLoader().getResourceAsStream("DataSet/diabetes.arff");
            if (arffStream == null) {
                // si el archivo no esta retorna que no lo encontro
                return "Error: No se encontró el archivo diabetes.arff";
            }

            // con esto leo el archivo
            BufferedReader reader = new BufferedReader(new InputStreamReader(arffStream));
            Instances data = new Instances(reader); // Convierte el contenido en un objeto Instances de Weka
            reader.close();

            // Establece la última columna del dataset como la clase a predecir
            data.setClassIndex(data.numAttributes() - 1);

            // Crea una nueva instancia (registro) con los mismos atributos del dataset
            Instance instance = new DenseInstance(data.numAttributes());
            instance.setDataset(data); // Asocia la instancia con el dataset

            // con esto le asigno un valor a cada uno de los valores al dataset
            instance.setValue(data.attribute("preg"), persona.getPregnancies());
            instance.setValue(data.attribute("plas"), persona.getPlasmaGlucose());
            instance.setValue(data.attribute("pres"), persona.getBloodPressure());
            instance.setValue(data.attribute("skin"), persona.getSkinThickness());
            instance.setValue(data.attribute("insu"), persona.getInsulin());
            instance.setValue(data.attribute("mass"), persona.getBmi());
            instance.setValue(data.attribute("pedi"), persona.getDiabetesPedigreeFunction());
            instance.setValue(data.attribute("age"), persona.getAge());

            // aqui cargamos el modelo entrenado del dataset
            InputStream modelStream = getClass().getClassLoader().getResourceAsStream("DataSet/diabetes.model");
            if (modelStream == null) {
                // Si no se encuentra el modelo, se devuelve un mensaje de error
                return "Error: No se encontró el archivo diabetes.model";
            }

            // con esto cargamos el clasifier el modelo entrenado de los datos
            Classifier classifier = (Classifier) SerializationHelper.read(modelStream);

            // con esto ustilisamos el clsififier para para que nos clasifique las intancias y guarde el resultado
            double resultado = classifier.classifyInstance(instance);

           //ahora guardamos en la clase el resultado del data set , el test_neasgtive o postive y lo convertimos en un string
            String clase = data.classAttribute().value((int) resultado);

            // ahora ese resultado se lo pasamos a la persona
            persona.setResultado(clase);

            // aqui asignamos a la persona con los datos ya clasificado , con su respuesta
            historialResultados.add(persona);

            // entonces este metodo solo retorna la clase que es donde esta test_negative o positive
            return clase;

        } catch (Exception e) {
            // Si ocurre un error, se imprime el error en consola y se devuelve un mensaje
            e.printStackTrace();
            return "Error durante la predicción: " + e.getMessage();
        }
    }

    //este metdod es el que retorina todos las repuesta en una lista
    @Override
    public List<Persona> obtenerHistorial() {
        return historialResultados;
    }

    //este metodo limpia todos los campos
    @Override
    public void eliminarHistorial() {
        historialResultados.clear();
    }

}
