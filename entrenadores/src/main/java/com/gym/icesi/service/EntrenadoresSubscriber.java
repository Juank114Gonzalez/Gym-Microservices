package com.gym.icesi.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntrenadoresSubscriber {
    @Autowired
    private EntrenadorService entrenadorService;

    @RabbitListener(queues = "inscripcion.queue")
    public void recibirNotificacion(String notificacion) {
        entrenadorService.enviarNotificacion(notificacion);
    }
}