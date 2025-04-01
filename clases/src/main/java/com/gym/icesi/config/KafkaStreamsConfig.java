package com.gym.icesi.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


import com.gym.icesi.model.DatosEntrenamiento;
import com.gym.icesi.model.ResumenEntrenamiento;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    /**
     * Configuración base para Kafka Streams
     */
    @Bean
    public KafkaStreamsConfiguration defaultKafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "datos-entrenamiento-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        
        return new KafkaStreamsConfiguration(props);
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME)
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildStreamsProperties());

        // Agregar configuraciones adicionales si es necesario
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "datos-entrenamiento");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(DatosEntrenamiento.class).getClass().getName());
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(config));
    }

    /**
     * Procesador de streams para el análisis de datos de entrenamiento
     * El flujo toma datos del topic "datos-entrenamiento", los agrega por ventana de 7 días,
     * y luego los envía al topic "resumen-entrenamiento"
     */
    @Bean
    public KStream<String, DatosEntrenamiento> kStream(StreamsBuilder streamsBuilder) {
        // Consumo de datos del tópico "datos-entrenamiento"
        KStream<String, DatosEntrenamiento> stream = streamsBuilder
                .stream("datos-entrenamiento", Consumed.with(Serdes.String(), new JsonSerde<>(DatosEntrenamiento.class)));

        // Procesamiento: Agrupar por clave, aplicar ventanas de 7 días, y generar resumen
        stream.groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(7)))
                .aggregate(
                        ResumenEntrenamiento::new,  // Inicializar resumen
                        (key, value, aggregate) -> aggregate.actualizar(value),
                        Materialized.as("resumen-entrenamiento-store"))
                .toStream()
                .to("resumen-entrenamiento");
        
        return stream;
    }
}
