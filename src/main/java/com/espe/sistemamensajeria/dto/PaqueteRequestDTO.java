package com.espe.sistemamensajeria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteRequestDTO {

    @NotBlank(message = "La descripción del paquete es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String descripcion;

    @NotNull(message = "El peso del paquete es obligatorio")
    @Positive(message = "El peso debe ser mayor a cero")
    private Double peso;

    @NotBlank(message = "La dirección de destino es obligatoria")
    @Size(max = 200, message = "La dirección de destino no puede superar los 200 caracteres")
    private String direccionDestino;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
}
