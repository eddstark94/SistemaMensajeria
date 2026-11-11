
package com.espe.sistemamensajeria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "paquetes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Descripción del contenido del paquete.
    @Column(nullable = false, length = 200)
    private String descripcion;

    // Peso del paquete expresado en kilogramos.
    @Column(nullable = false)
    private Double peso;

    // Dirección donde debe entregarse el paquete.
    @Column(nullable = false, length = 200)
    private String direccionDestino;

    // Cliente al que pertenece el paquete.
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}

