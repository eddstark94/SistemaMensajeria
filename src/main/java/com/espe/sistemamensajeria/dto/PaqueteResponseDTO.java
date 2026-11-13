package com.espe.sistemamensajeria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteResponseDTO {
    private Long id;
    private String descripcion;
    private Double peso;
    private String direccionDestino;
    private Long clienteId;
    private String clienteNombre;
}
