
package com.espe.sistemamensajeria.repository;

import com.espe.sistemamensajeria.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Permite administrar las entregas almacenadas en MySQL.
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    // Buscar entregas por estado.
    List<Entrega> findByEstadoIgnoreCase(String estado);

}
