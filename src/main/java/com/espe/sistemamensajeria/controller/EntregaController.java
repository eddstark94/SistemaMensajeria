package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.dto.EntregaRequestDTO;
import com.espe.sistemamensajeria.dto.EntregaResponseDTO;
import com.espe.sistemamensajeria.entity.Entrega;
import com.espe.sistemamensajeria.entity.Paquete;
import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.service.EntregaService;
import com.espe.sistemamensajeria.service.PaqueteService;
import com.espe.sistemamensajeria.service.RepartidorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;
    private final PaqueteService paqueteService;
    private final RepartidorService repartidorService;

    @PostMapping
    public ResponseEntity<EntregaResponseDTO> crear(@Valid @RequestBody EntregaRequestDTO request) {
        Paquete paquete = paqueteService.buscarPorId(request.getPaqueteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paquete no encontrado con ID: " + request.getPaqueteId()));

        Repartidor repartidor = null;
        if (request.getRepartidorId() != null) {
            repartidor = repartidorService.buscarPorId(request.getRepartidorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Repartidor no encontrado con ID: " + request.getRepartidorId()));
        }

        Entrega entrega = new Entrega();
        entrega.setEstado(request.getEstado());
        entrega.setPaquete(paquete);
        entrega.setRepartidor(repartidor);

        Entrega guardada = entregaService.guardar(entrega);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(guardada));
    }

    @GetMapping
    public ResponseEntity<List<EntregaResponseDTO>> listarTodas() {
        List<EntregaResponseDTO> lista = entregaService.listarTodas()
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // Consultar entregas pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<EntregaResponseDTO>> listarPendientes() {

        List<EntregaResponseDTO> entregas = entregaService.listarPendientes()
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntregaResponseDTO> obtenerPorId(@PathVariable Long id) {
        Entrega entrega = entregaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con ID: " + id));
        return ResponseEntity.ok(toResponseDTO(entrega));
    }

    // PATCH /api/v1/entregas/{id}/estado?estado=EN_CAMINO
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntregaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        Entrega actualizada = entregaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(toResponseDTO(actualizada));
    }

    // Asignar un repartidor a una entrega existente
    @PatchMapping("/{id}/repartidor/{repartidorId}")
    public ResponseEntity<EntregaResponseDTO> asignarRepartidor(
            @PathVariable Long id,
            @PathVariable Long repartidorId) {
        Repartidor repartidor = repartidorService.buscarPorId(repartidorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Repartidor no encontrado con ID: " + repartidorId));
        Entrega actualizada = entregaService.asignarRepartidor(id, repartidor);
        return ResponseEntity.ok(toResponseDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        entregaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con ID: " + id));
        entregaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private EntregaResponseDTO toResponseDTO(Entrega entrega) {
        return EntregaResponseDTO.builder()
                .id(entrega.getId())
                .estado(entrega.getEstado())
                .fechaEntrega(entrega.getFechaEntrega())
                .paqueteId(entrega.getPaquete() != null ? entrega.getPaquete().getId() : null)
                .paqueteDescripcion(entrega.getPaquete() != null ? entrega.getPaquete().getDescripcion() : null)
                .direccionDestino(entrega.getPaquete() != null ? entrega.getPaquete().getDireccionDestino() : null)
                .repartidorId(entrega.getRepartidor() != null ? entrega.getRepartidor().getId() : null)
                .repartidorNombre(entrega.getRepartidor() != null ? entrega.getRepartidor().getNombre() : null)
                .build();
    }
}
