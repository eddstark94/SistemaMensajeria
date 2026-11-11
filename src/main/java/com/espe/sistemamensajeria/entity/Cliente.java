
package com.espe.sistemamensajeria.entity;

// Importamos las anotaciones necesarias para trabajar con JPA.
import jakarta.persistence.*;
// Importamos Lombok para generar automáticamente métodos y constructores.
import lombok.*;


// Indicamos que esta clase representa una entidad que será almacenada
// como una tabla dentro de la base de datos.
@Entity

// Definimos el nombre de la tabla en MySQL.
@Table(name = "clientes")

// Genera automáticamente los métodos Getters y Setters.
@Getter
@Setter

// Genera un constructor sin parámetros.
@NoArgsConstructor

// Genera un constructor con todos los atributos.
@AllArgsConstructor

public class Cliente {

    // Identificador principal de cada cliente (Primary Key).
    @Id

    // Permite que MySQL genere automáticamente el identificador.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del cliente.
    @Column(nullable = false, length = 100)
    private String nombre;

    // Correo electrónico del cliente.
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    // Número telefónico del cliente.
    @Column(nullable = false, length = 15)
    private String telefono;

    // Dirección del cliente.
    @Column(nullable = false, length = 200)
    private String direccion;
}
