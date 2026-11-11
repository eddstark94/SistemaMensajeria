package com.espe.sistemamensajeria.repository;

import com.espe.sistemamensajeria.entity.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;

// Permite administrar los repartidores almacenados en MySQL.
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
}
