// Paquete donde se encuentra esta clase
package com.weka.wekaProyecto.servicesIMP;

// Importación de la entidad Persona y del servicio de recomendación
import com.weka.wekaProyecto.entities.Persona;
import com.weka.wekaProyecto.service.RecomendacionService;

// Librerías necesarias para trabajar con JSON y HTTP
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Esta clase se marca como un servicio de Spring para que pueda ser inyectada donde se necesite
@Service
public class RecomendacionServiceImpl implements RecomendacionService {

    // Se obtiene la clave API de OpenRouter desde el archivo application.properties
    @Value("${openrouter.api.key2}")
    private String OPENAI_API_KEY;

    // URL base del endpoint de la API de OpenRouter
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

    // este es el metodo que genera una recomendacion
    @Override
    public String generarRecomendacion(Persona persona) {
        // si es test es postivo genera la recomendacion si no felicita
        if ("tested_positive".equalsIgnoreCase(persona.getResultado())) {
            //evalua ese igual si esta enferma retorna el metodo que obtiene el consejo desde la IA
            return obtenerRecomendacionDesdeOpenRouter(persona);
        } else {
            // Si el resultado es negativo, se muestra un mensaje de felicitación
            return "✅ ¡Felicidades! No se detectaron indicios de diabetes. Sigue llevando un estilo de vida saludable 🥗🏃‍♂️.";
        }
    }

    // este es el metodo que genera una recomendacion de la IA
    private String obtenerRecomendacionDesdeOpenRouter(Persona persona) {
        try {
            // Generamos un promt para la iA con los datos mas relevante de la persona que los toma de la entidad person
            String prompt = "Una persona ha sido diagnosticada como 'tested_positive' para diabetes. Sus datos son: " +
                    "Edad: " + persona.getAge() +
                    ", Glucosa: " + persona.getPlasmaGlucose() +
                    ", Presión arterial: " + persona.getBloodPressure() +
                    ", IMC: " + persona.getBmi() + ". " +
                    "Dale una recomendación médica general, amigable y práctica para mejorar su salud.";

            // Cuerpo del request en formato JSON, compatible con la API de OpenRouter
            String bodyJson = """
            {
              "model": "openai/gpt-3.5-turbo",
              "messages": [
                { "role": "system", "content": "Eres un médico virtual experto en diabetes." },
                { "role": "user", "content": "%s" }
              ],
              "temperature": 0.8
            }
            """.formatted(prompt); //le añadimos el promt con los datps del cliente

            // aqui se contruye la peticcion http
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENROUTER_URL)) //aqui esta la url de la ia
                    .header("Content-Type", "application/json") // Tipo de contenido
                    .header("Authorization", "Bearer " + OPENAI_API_KEY) // aqii esta la llave que genro la ia
                    .header("HTTP-Referer", "http://localhost:8080") //aquie le paso el localhost donde estmos
                    .header("X-Title", "recomendador-diabetes") // Título del proyecto para OpenRouter
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson)) //utilizamos el post para enviar los datos y le enviamos el json con el promt
                    .build();

            // con esto utilizamos ewl post para enviar los datos
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // con esto se obtiene la respuesta de la ia|
            String respuestaJson = response.body();

            // Imprime la respuesta completa para depuración
            System.out.println("🧾 Respuesta JSON completa:");
            System.out.println(respuestaJson);

            // Se extrae y devuelve la recomendación generada por la IA
            return extraerRespuestaDeJson(respuestaJson);
        } catch (Exception e) {
            // En caso de error, se imprime la traza y se devuelve un mensaje de error
            e.printStackTrace();
            return "❌ Error al generar recomendación: " + e.getMessage();
        }
    }

    // este metofdo convierte la respuesta de la ia en string
    private String extraerRespuestaDeJson(String json) {
        try {
            // Se parsea el JSON
            JSONObject obj = new JSONObject(json);

            // Si hay un error, se devuelve el mensaje de error
            if (obj.has("error")) {
                JSONObject error = obj.getJSONObject("error");
                return "⚠️ Error del servidor: " + error.optString("message", "Error desconocido");
            }

            // Se accede al contenido generado por el modelo
            JSONArray choices = obj.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content").trim(); //si todo esta bien retrona el mensaje de la AI
        } catch (Exception e) {
            // En caso de error al leer el JSON, se devuelve un mensaje predeterminado
            System.out.println("❌ Error al parsear la respuesta JSON:");
            e.printStackTrace();
            return "⚠️ No se pudo generar una recomendación. Inténtalo más tarde.";
        }
    }
}
