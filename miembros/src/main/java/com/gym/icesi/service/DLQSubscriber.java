package com.gym.icesi.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class DLQSubscriber {    
    @Autowired
    private MiembroService miembroService;
    
    @RabbitListener(queues = "dlq")
    public void processFailedMessages(String notificacion) {
        miembroService.enviarNotificacion("⛔" + notificacion);
    }
}
