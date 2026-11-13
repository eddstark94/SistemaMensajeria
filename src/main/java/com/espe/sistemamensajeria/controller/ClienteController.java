package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.dto.ClienteRequestDTO;
import com.espe.sistemamensajeria.dto.ClienteResponseDTO;
import com.espe.sistemamensajeria.entity.Cliente;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        Cliente guardado = clienteService.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(guardado));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> lista = clienteService.listarTodos()
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return ResponseEntity.ok(toResponseDTO(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO request) {
        Cliente datosActualizados = new Cliente();
        datosActualizados.setNombre(request.getNombre());
        datosActualizados.setCorreo(request.getCorreo());
        datosActualizados.setTelefono(request.getTelefono());
        datosActualizados.setDireccion(request.getDireccion());

        Cliente actualizado = clienteService.actualizar(id, datosActualizados);
        return ResponseEntity.ok(toResponseDTO(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .correo(cliente.getCorreo())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .build();
    }
}
