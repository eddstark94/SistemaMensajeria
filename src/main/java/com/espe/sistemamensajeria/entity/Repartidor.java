
package com.espe.sistemamensajeria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "repartidores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repartidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre completo del repartidor.
    @Column(nullable = false, length = 100)
    private String nombre;

    // Número telefónico de contacto.
    @Column(nullable = false, length = 15)
    private String telefono;

    // Indica si el repartidor está disponible.
    @Column(nullable = false)
    private Boolean disponible;
}

