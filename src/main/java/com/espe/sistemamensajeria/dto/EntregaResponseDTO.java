package com.espe.sistemamensajeria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregaResponseDTO {
    private Long id;
    private String estado;
    private LocalDateTime fechaEntrega;
    private Long paqueteId;
    private String paqueteDescripcion;
    private String direccionDestino;
    private Long repartidorId;
    private String repartidorNombre;
}
