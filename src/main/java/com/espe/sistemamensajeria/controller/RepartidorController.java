package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.dto.RepartidorRequestDTO;
import com.espe.sistemamensajeria.dto.RepartidorResponseDTO;
import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.service.RepartidorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repartidores")
@RequiredArgsConstructor
public class RepartidorController {

    private final RepartidorService repartidorService;

    @PostMapping
    public ResponseEntity<RepartidorResponseDTO> crear(@Valid @RequestBody RepartidorRequestDTO request) {
        Repartidor repartidor = new Repartidor();
        repartidor.setNombre(request.getNombre());
        repartidor.setTelefono(request.getTelefono());
        repartidor.setDisponible(request.getDisponible());

        Repartidor guardado = repartidorService.guardar(repartidor);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(guardado));
    }

    @GetMapping
    public ResponseEntity<List<RepartidorResponseDTO>> listarTodos() {
        List<RepartidorResponseDTO> lista = repartidorService.listarTodos()
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepartidorResponseDTO> obtenerPorId(@PathVariable Long id) {
        Repartidor repartidor = repartidorService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Repartidor no encontrado con ID: " + id));
        return ResponseEntity.ok(toResponseDTO(repartidor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepartidorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RepartidorRequestDTO request) {
        Repartidor datosActualizados = new Repartidor();
        datosActualizados.setNombre(request.getNombre());
        datosActualizados.setTelefono(request.getTelefono());
        datosActualizados.setDisponible(request.getDisponible());

        Repartidor actualizado = repartidorService.actualizar(id, datosActualizados);
        return ResponseEntity.ok(toResponseDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        repartidorService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Repartidor no encontrado con ID: " + id));
        repartidorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RepartidorResponseDTO toResponseDTO(Repartidor repartidor) {
        return RepartidorResponseDTO.builder()
                .id(repartidor.getId())
                .nombre(repartidor.getNombre())
                .telefono(repartidor.getTelefono())
                .disponible(repartidor.getDisponible())
                .build();
    }
}
