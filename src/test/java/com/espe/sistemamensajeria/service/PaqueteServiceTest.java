
package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Paquete;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.repository.PaqueteRepository;

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
class PaqueteServiceTest {

    @Mock
    private PaqueteRepository paqueteRepository;

    @InjectMocks
    private PaqueteService paqueteService;

    @Test
    void listarTodosDebeRetornarPaquetes() {
        when(paqueteRepository.findAll())
                .thenReturn(List.of(new Paquete()));

        List<Paquete> resultado = paqueteService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorIdDebeRetornarPaquete() {
        Paquete paquete = new Paquete();

        when(paqueteRepository.findById(1L))
                .thenReturn(Optional.of(paquete));

        Optional<Paquete> resultado = paqueteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(paquete, resultado.get());
    }

    @Test
    void guardarDebeGuardarPaquete() {
        Paquete paquete = new Paquete();

        when(paqueteRepository.save(paquete)).thenReturn(paquete);

        Paquete resultado = paqueteService.guardar(paquete);

        assertEquals(paquete, resultado);
        verify(paqueteRepository).save(paquete);
    }

    @Test
    void actualizarDebeModificarPaqueteExistente() {
        Paquete paquete = new Paquete();
        Paquete actualizado = new Paquete();

        actualizado.setDescripcion("Caja de documentos");
        actualizado.setPeso(2.5);
        actualizado.setDireccionDestino("Quito");

        when(paqueteRepository.findById(1L))
                .thenReturn(Optional.of(paquete));
        when(paqueteRepository.save(paquete)).thenReturn(paquete);

        Paquete resultado = paqueteService.actualizar(1L, actualizado);

        assertEquals("Caja de documentos", resultado.getDescripcion());
        assertEquals(2.5, resultado.getPeso());
        assertEquals("Quito", resultado.getDireccionDestino());
    }

    @Test
    void eliminarDebeBorrarPaqueteExistente() {
        Paquete paquete = new Paquete();

        when(paqueteRepository.findById(1L))
                .thenReturn(Optional.of(paquete));

        paqueteService.eliminar(1L);

        verify(paqueteRepository).delete(paquete);
    }

    @Test
    void actualizarDebeLanzarExcepcionSiPaqueteNoExiste() {
        when(paqueteRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> paqueteService.actualizar(999L, new Paquete())
        );
    }
}
