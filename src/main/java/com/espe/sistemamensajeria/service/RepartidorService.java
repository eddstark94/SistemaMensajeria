package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.repository.RepartidorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepartidorService {

    private final RepartidorRepository repartidorRepository;

    public RepartidorService(RepartidorRepository repartidorRepository) {
        this.repartidorRepository = repartidorRepository;
    }

    // Listar repartidores
    public List<Repartidor> listarTodos() {
        return repartidorRepository.findAll();
    }

    // Buscar repartidor por ID
    public Optional<Repartidor> buscarPorId(Long id) {
        return repartidorRepository.findById(id);
    }

    // Guardar repartidor
    public Repartidor guardar(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }

    // Actualizar repartidor
    public Repartidor actualizar(Long id,
                                 Repartidor repartidorActualizado) {

        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repartidor no encontrado"));

        repartidor.setNombre(repartidorActualizado.getNombre());
        repartidor.setTelefono(repartidorActualizado.getTelefono());
        repartidor.setDisponible(
                repartidorActualizado.getDisponible());

        return repartidorRepository.save(repartidor);
    }
    // Eliminar repartidor
    public void eliminar(Long id) {
        repartidorRepository.deleteById(id);
    }
}