package com.gym.icesi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosEntrenamiento {
    private String id;
    private String usuarioId;
    private String tipoEjercicio;
    private int duracion;
    private int caloriasQuemadas;
    private int frecuenciaCardiacaPromedio;
}
