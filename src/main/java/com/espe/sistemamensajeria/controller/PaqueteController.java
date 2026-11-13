package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.dto.PaqueteRequestDTO;
import com.espe.sistemamensajeria.dto.PaqueteResponseDTO;
import com.espe.sistemamensajeria.entity.Cliente;
import com.espe.sistemamensajeria.entity.Paquete;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.service.ClienteService;
import com.espe.sistemamensajeria.service.PaqueteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paquetes")
@RequiredArgsConstructor
public class PaqueteController {

    private final PaqueteService paqueteService;
    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<PaqueteResponseDTO> crear(@Valid @RequestBody PaqueteRequestDTO request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con ID: " + request.getClienteId()));

        Paquete paquete = new Paquete();
        paquete.setDescripcion(request.getDescripcion());
        paquete.setPeso(request.getPeso());
        paquete.setDireccionDestino(request.getDireccionDestino());
        paquete.setCliente(cliente);

        Paquete guardado = paqueteService.guardar(paquete);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(guardado));
    }

    @GetMapping
    public ResponseEntity<List<PaqueteResponseDTO>> listarTodos() {
        List<PaqueteResponseDTO> lista = paqueteService.listarTodos()
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteResponseDTO> obtenerPorId(@PathVariable Long id) {
        Paquete paquete = paqueteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete no encontrado con ID: " + id));
        return ResponseEntity.ok(toResponseDTO(paquete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaqueteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PaqueteRequestDTO request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con ID: " + request.getClienteId()));

        Paquete datosActualizados = new Paquete();
        datosActualizados.setDescripcion(request.getDescripcion());
        datosActualizados.setPeso(request.getPeso());
        datosActualizados.setDireccionDestino(request.getDireccionDestino());
        datosActualizados.setCliente(cliente);

        Paquete actualizado = paqueteService.actualizar(id, datosActualizados);
        return ResponseEntity.ok(toResponseDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        paqueteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete no encontrado con ID: " + id));
        paqueteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private PaqueteResponseDTO toResponseDTO(Paquete paquete) {
        return PaqueteResponseDTO.builder()
                .id(paquete.getId())
                .descripcion(paquete.getDescripcion())
                .peso(paquete.getPeso())
                .direccionDestino(paquete.getDireccionDestino())
                .clienteId(paquete.getCliente() != null ? paquete.getCliente().getId() : null)
                .clienteNombre(paquete.getCliente() != null ? paquete.getCliente().getNombre() : null)
                .build();
    }
}
