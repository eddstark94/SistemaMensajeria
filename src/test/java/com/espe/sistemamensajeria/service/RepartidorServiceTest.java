
package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.repository.RepartidorRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepartidorServiceTest {

    @Mock
    private RepartidorRepository repartidorRepository;

    @InjectMocks
    private RepartidorService repartidorService;

    @Test
    void listarTodosDebeRetornarRepartidores() {
        when(repartidorRepository.findAll())
                .thenReturn(List.of(new Repartidor()));

        List<Repartidor> resultado = repartidorService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorIdDebeRetornarRepartidor() {
        Repartidor repartidor = new Repartidor();

        when(repartidorRepository.findById(1L))
                .thenReturn(Optional.of(repartidor));

        Optional<Repartidor> resultado =
                repartidorService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(repartidor, resultado.get());
    }

    @Test
    void guardarDebeGuardarRepartidor() {
        Repartidor repartidor = new Repartidor();

        when(repartidorRepository.save(repartidor))
                .thenReturn(repartidor);

        Repartidor resultado = repartidorService.guardar(repartidor);

        assertEquals(repartidor, resultado);
        verify(repartidorRepository).save(repartidor);
    }

    @Test
    void actualizarDebeModificarRepartidorExistente() {
        Repartidor repartidor = new Repartidor();
        Repartidor actualizado = new Repartidor();

        actualizado.setNombre("Carlos Ramirez");
        actualizado.setTelefono("0987654321");
        actualizado.setDisponible(false);

        when(repartidorRepository.findById(1L))
                .thenReturn(Optional.of(repartidor));
        when(repartidorRepository.save(repartidor))
                .thenReturn(repartidor);

        Repartidor resultado =
                repartidorService.actualizar(1L, actualizado);

        assertEquals("Carlos Ramirez", resultado.getNombre());
        assertEquals("0987654321", resultado.getTelefono());
        assertEquals(false, resultado.getDisponible());
    }

    @Test
    void eliminarDebeBorrarRepartidorExistente() {
        Repartidor repartidor = new Repartidor();

        when(repartidorRepository.findById(1L))
                .thenReturn(Optional.of(repartidor));

        repartidorService.eliminar(1L);

        verify(repartidorRepository).delete(repartidor);
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {
        when(repartidorRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> repartidorService.actualizar(
                        999L, new Repartidor())
        );
    }
}
