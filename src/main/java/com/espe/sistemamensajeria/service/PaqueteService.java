package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Paquete;
import com.espe.sistemamensajeria.repository.PaqueteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaqueteService {

    private final PaqueteRepository paqueteRepository;

    public PaqueteService(PaqueteRepository paqueteRepository) {
        this.paqueteRepository = paqueteRepository;
    }

    // Listar todos los paquetes
    public List<Paquete> listarTodos() {
        return paqueteRepository.findAll();
    }

    // Buscar paquete por ID
    public Optional<Paquete> buscarPorId(Long id) {
        return paqueteRepository.findById(id);
    }

    // Guardar paquete
    public Paquete guardar(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    // Actualizar paquete
    public Paquete actualizar(Long id, Paquete paqueteActualizado) {

        Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Paquete no encontrado"));

        paquete.setDescripcion(paqueteActualizado.getDescripcion());
        paquete.setPeso(paqueteActualizado.getPeso());
        paquete.setDireccionDestino(
                paqueteActualizado.getDireccionDestino());
        paquete.setCliente(paqueteActualizado.getCliente());

        return paqueteRepository.save(paquete);
    }
    // Eliminar paquete
    public void eliminar(Long id) {
        paqueteRepository.deleteById(id);
    }
}