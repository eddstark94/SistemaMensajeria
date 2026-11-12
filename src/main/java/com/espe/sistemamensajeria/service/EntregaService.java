package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Entrega;
import com.espe.sistemamensajeria.repository.EntregaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;

    public EntregaService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    // Listar todas las entregas
    public List<Entrega> listarTodas() {
        return entregaRepository.findAll();
    }

    // Buscar una entrega por ID
    public Optional<Entrega> buscarPorId(Long id) {
        return entregaRepository.findById(id);
    }

    // Guardar una nueva entrega
    public Entrega guardar(Entrega entrega) {

        // Estado inicial por defecto
        if (entrega.getEstado() == null ||
                entrega.getEstado().isBlank()) {

            entrega.setEstado("PENDIENTE");
        }

        // No permitir asignar repartidores no disponibles
        if (entrega.getRepartidor() != null &&
                !entrega.getRepartidor().getDisponible()) {

            throw new RuntimeException(
                    "El repartidor no esta disponible");
        }

        return entregaRepository.save(entrega);
    }

    // Actualizar estado de la entrega
    public Entrega actualizarEstado(Long id, String estadoNuevo) {

        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Entrega no encontrada"));

        String estadoActual = entrega.getEstado();

        // No permitir modificar una entrega ya completada
        if ("ENTREGADO".equalsIgnoreCase(estadoActual)) {

            throw new RuntimeException(
                    "La entrega ya fue completada");
        }

        // No permitir pasar directamente de
        // PENDIENTE a ENTREGADO
        if ("PENDIENTE".equalsIgnoreCase(estadoActual)
                && "ENTREGADO".equalsIgnoreCase(estadoNuevo)) {

            throw new RuntimeException(
                    "Debe pasar por EN_CAMINO antes de ENTREGADO");
        }

        // Registrar fecha automáticamente al entregar
        if ("ENTREGADO".equalsIgnoreCase(estadoNuevo)) {

            entrega.setFechaEntrega(
                    LocalDateTime.now());
        }

        entrega.setEstado(estadoNuevo);

        return entregaRepository.save(entrega);
    }

    // Eliminar entrega
    public void eliminar(Long id) {
        entregaRepository.deleteById(id);
    }
}