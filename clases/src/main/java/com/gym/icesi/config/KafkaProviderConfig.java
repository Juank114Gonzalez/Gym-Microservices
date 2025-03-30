package co.edu.icesi.config; 

import com.gym.icesi.dto.ClaseRegistradaDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer; 

import java.util.HashMap;
import java.util.Map;
 
@Configuration
public class KafkaProviderConfig { 

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers; 

    public Map<String, Object> producerConfig(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return properties;
    } 

    @Bean
    public ProducerFactory<String, ClaseRegistradaDTO> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    } 

    @Bean
    public KafkaTemplate<String, ClaseRegistradaDTO> kafkaTemplate(ProducerFactory<String, ClaseRegistradaDTO> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

}
