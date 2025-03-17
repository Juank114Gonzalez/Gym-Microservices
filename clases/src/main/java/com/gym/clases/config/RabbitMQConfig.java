package com.gym.clases.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.DirectExchange;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue horarioQueue() {
        return QueueBuilder.durable("horario.queue")
        .withArgument("x-dead-letter-exchange", "dlx-exchange")
        .withArgument("x-dead-letter-routing-key", "dlq")
        .build();
    }


    @Bean
    public Queue dlq() {
        return QueueBuilder.durable("dlq").build();
    }
    
    @Bean
    public Queue inscripcionQueue() {
        return new Queue("inscripcion.queue", true);
    }
    
    @Bean
    public TopicExchange claseExchange() {
        return new TopicExchange("clase.exchange");
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlx-exchange");
    }


    @Bean
    public Binding bindingHorario(Queue horarioQueue, TopicExchange claseExchange) {
        return BindingBuilder.bind(horarioQueue).to(claseExchange).with("horario.routingkey");
    }


    @Bean
    public Binding bindingInscripcion(Queue inscripcionQueue, TopicExchange claseExchange) {
        return BindingBuilder.bind(inscripcionQueue).to(claseExchange).with("inscripcion.routingkey");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(dlxExchange()).with("dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}