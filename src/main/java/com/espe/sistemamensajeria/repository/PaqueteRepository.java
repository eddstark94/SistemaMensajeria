package com.espe.sistemamensajeria.repository;

import com.espe.sistemamensajeria.entity.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;

// Permite administrar los paquetes almacenados en MySQL.
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
}

