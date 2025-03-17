package com.gym.miembros.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiembroSubscriber {
    @Autowired
    private MiembroService miembroService;

    @RabbitListener(queues = "horario.queue")
    public void recibirNotificacion(String notificacion) {
        miembroService.enviarNotificacion(notificacion);
    }
}