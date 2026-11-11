
package com.espe.sistemamensajeria.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Estado actual de la entrega.
    @Column(nullable = false, length = 30)
    private String estado;

    // Fecha y hora en que se completó la entrega.
    private LocalDateTime fechaEntrega;

    // Paquete que se debe entregar.
    @OneToOne
    @JoinColumn(name = "paquete_id", nullable = false, unique = true)
    private Paquete paquete;

    // Repartidor encargado de la entrega.
    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Repartidor repartidor;
}
