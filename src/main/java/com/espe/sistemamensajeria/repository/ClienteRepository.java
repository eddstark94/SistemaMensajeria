
package com.espe.sistemamensajeria.repository;

import com.espe.sistemamensajeria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// Permite realizar operaciones sobre los clientes en MySQL.
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

