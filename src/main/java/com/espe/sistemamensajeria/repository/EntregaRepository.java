
package com.espe.sistemamensajeria.repository;

import com.espe.sistemamensajeria.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

// Permite administrar las entregas almacenadas en MySQL.
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
}
