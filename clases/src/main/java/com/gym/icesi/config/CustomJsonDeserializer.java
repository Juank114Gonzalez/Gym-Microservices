package com.gym.icesi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.icesi.dto.ClaseRegistradaDTO;
import com.gym.icesi.model.DatosEntrenamiento;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CustomJsonDeserializer implements Deserializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Configuración si es necesario
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        
        try {
            // Convertir los bytes a string para inspeccionar el contenido
            String json = new String(data);

            // Comprobar si el JSON contiene campos específicos de ClaseRegistradaDTO
            if (json.contains("capacidadMaxima") && json.contains("nombreEntrenador")) {
                return objectMapper.readValue(data, ClaseRegistradaDTO.class);
            }

            // Comprobar si el JSON contiene campos específicos de DatosEntrenamiento
            if (json.contains("caloriasQuemadas") && json.contains("frecuenciaCardiacaPromedio")) {
                return objectMapper.readValue(data, DatosEntrenamiento.class);
            }

            // Si no se reconoce el tipo, lanzar una excepción
            throw new IllegalArgumentException("No se pudo determinar el tipo de mensaje para el JSON recibido.");

        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializando el mensaje de Kafka", e);
        }
    }

    @Override
    public void close() {
        // Cerrar recursos si es necesario
    }
}
