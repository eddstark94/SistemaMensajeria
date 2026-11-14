
package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Entrega;
import com.espe.sistemamensajeria.exception.BusinessRuleException;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.repository.EntregaRepository;
import org.springframework.stereotype.Service;
import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.exception.BusinessRuleException;

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

    // Listar entregas pendientes
    public List<Entrega> listarPendientes() {
        return entregaRepository.findByEstadoIgnoreCase("PENDIENTE");
    }

    // Asignar un repartidor a una entrega existente
    public Entrega asignarRepartidor(Long entregaId, Repartidor repartidor) {

        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Entrega no encontrada"));

        if (!Boolean.TRUE.equals(repartidor.getDisponible())) {
            throw new BusinessRuleException(
                    "El repartidor no está disponible");
        }

        if ("ENTREGADO".equalsIgnoreCase(entrega.getEstado())) {
            throw new BusinessRuleException(
                    "No se puede asignar un repartidor a una entrega completada");
        }

        entrega.setRepartidor(repartidor);

        return entregaRepository.save(entrega);
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

        // Validar disponibilidad del repartidor
        if (entrega.getRepartidor() != null &&
                !Boolean.TRUE.equals(
                        entrega.getRepartidor().getDisponible())) {

            throw new BusinessRuleException(
                    "El repartidor no está disponible");
        }

        return entregaRepository.save(entrega);
    }

    // Actualizar estado de la entrega
    public Entrega actualizarEstado(Long id, String estadoNuevo) {

        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Entrega no encontrada"));

        if (estadoNuevo == null || estadoNuevo.isBlank()) {
            throw new BusinessRuleException(
                    "El nuevo estado es obligatorio");
        }

        estadoNuevo = estadoNuevo.trim().toUpperCase();

        String estadoActual = entrega.getEstado();

        // Validar que el estado sea permitido
        if (!estadoNuevo.equals("PENDIENTE") &&
                !estadoNuevo.equals("EN_CAMINO") &&
                !estadoNuevo.equals("ENTREGADO")) {

            throw new BusinessRuleException(
                    "El estado indicado no es válido");
        }

        // Validar las transiciones permitidas
        if ("ENTREGADO".equals(estadoActual)) {
            throw new BusinessRuleException(
                    "La entrega ya fue completada");
        }

        if ("PENDIENTE".equals(estadoActual) &&
                !"EN_CAMINO".equals(estadoNuevo)) {

            throw new BusinessRuleException(
                    "Una entrega pendiente debe pasar a EN_CAMINO");
        }

        if ("EN_CAMINO".equals(estadoActual) &&
                !"ENTREGADO".equals(estadoNuevo)) {

            throw new BusinessRuleException(
                    "Una entrega en camino debe pasar a ENTREGADO");
        }

        // Registrar fecha al completar la entrega
        if ("ENTREGADO".equals(estadoNuevo)) {
            entrega.setFechaEntrega(LocalDateTime.now());
        }

        entrega.setEstado(estadoNuevo);

        return entregaRepository.save(entrega);
    }

    // Eliminar entrega
    public void eliminar(Long id) {

        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Entrega no encontrada"));

        entregaRepository.delete(entrega);
    }
}
