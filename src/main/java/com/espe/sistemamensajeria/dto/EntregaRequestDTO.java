package com.espe.sistemamensajeria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregaRequestDTO {

    @NotBlank(message = "El estado inicial de la entrega es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    private String estado;

    @NotNull(message = "El ID del paquete es obligatorio")
    private Long paqueteId;

    // Opcional al crear la entrega; puede asignarse después con PATCH
    private Long repartidorId;
}
