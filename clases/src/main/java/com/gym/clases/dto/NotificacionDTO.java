package com.gym.clases.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NotificacionDTO implements Serializable {
    private String mensaje;
}
