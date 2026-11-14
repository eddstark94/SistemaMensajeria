
package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Entrega;
import com.espe.sistemamensajeria.entity.Repartidor;
import com.espe.sistemamensajeria.exception.BusinessRuleException;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.repository.EntregaRepository;

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
class EntregaServiceTest {

    @Mock
    private EntregaRepository entregaRepository;

    @InjectMocks
    private EntregaService entregaService;

    @Test
    void listarTodasDebeRetornarEntregas() {
        when(entregaRepository.findAll())
                .thenReturn(List.of(new Entrega()));

        List<Entrega> resultado = entregaService.listarTodas();

        assertEquals(1, resultado.size());
    }

    @Test
    void listarPendientesDebeConsultarEstadoPendiente() {
        when(entregaRepository.findByEstadoIgnoreCase("PENDIENTE"))
                .thenReturn(List.of(new Entrega()));

        List<Entrega> resultado = entregaService.listarPendientes();

        assertEquals(1, resultado.size());
        verify(entregaRepository).findByEstadoIgnoreCase("PENDIENTE");
    }

    @Test
    void guardarDebeAsignarEstadoPendientePorDefecto() {
        Entrega entrega = new Entrega();

        when(entregaRepository.save(entrega)).thenReturn(entrega);

        Entrega resultado = entregaService.guardar(entrega);

        assertEquals("PENDIENTE", resultado.getEstado());
        verify(entregaRepository).save(entrega);
    }

    @Test
    void guardarDebeRechazarRepartidorNoDisponible() {
        Entrega entrega = new Entrega();
        Repartidor repartidor = new Repartidor();
        repartidor.setDisponible(false);
        entrega.setRepartidor(repartidor);

        assertThrows(
                BusinessRuleException.class,
                () -> entregaService.guardar(entrega)
        );

        verify(entregaRepository, never()).save(any());
    }

    @Test
    void actualizarEstadoDebePermitirPasarAEnCamino() {
        Entrega entrega = new Entrega();
        entrega.setEstado("PENDIENTE");

        when(entregaRepository.findById(1L))
                .thenReturn(Optional.of(entrega));
        when(entregaRepository.save(entrega)).thenReturn(entrega);

        Entrega resultado =
                entregaService.actualizarEstado(1L, "EN_CAMINO");

        assertEquals("EN_CAMINO", resultado.getEstado());
    }

    @Test
    void actualizarEstadoDebeRegistrarFechaAlEntregar() {
        Entrega entrega = new Entrega();
        entrega.setEstado("EN_CAMINO");

        when(entregaRepository.findById(1L))
                .thenReturn(Optional.of(entrega));
        when(entregaRepository.save(entrega)).thenReturn(entrega);

        Entrega resultado =
                entregaService.actualizarEstado(1L, "ENTREGADO");

        assertEquals("ENTREGADO", resultado.getEstado());
        assertNotNull(resultado.getFechaEntrega());
    }

    @Test
    void actualizarEstadoDebeRechazarTransicionInvalida() {
        Entrega entrega = new Entrega();
        entrega.setEstado("PENDIENTE");

        when(entregaRepository.findById(1L))
                .thenReturn(Optional.of(entrega));

        assertThrows(
                BusinessRuleException.class,
                () -> entregaService.actualizarEstado(1L, "ENTREGADO")
        );

        verify(entregaRepository, never()).save(any());
    }

    @Test
    void buscarPorIdDebeRetornarVacioSiNoExiste() {
        when(entregaRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertTrue(entregaService.buscarPorId(999L).isEmpty());
    }

    @Test
    void actualizarEstadoDebeLanzarExcepcionSiNoExiste() {
        when(entregaRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> entregaService.actualizarEstado(999L, "EN_CAMINO")
        );
    }

    @Test
    void eliminarDebeBorrarEntregaExistente() {
        Entrega entrega = new Entrega();

        when(entregaRepository.findById(1L))
                .thenReturn(Optional.of(entrega));

        entregaService.eliminar(1L);

        verify(entregaRepository).delete(entrega);
    }
}
