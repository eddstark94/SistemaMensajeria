
package com.espe.sistemamensajeria.service;

import com.espe.sistemamensajeria.entity.Cliente;
import com.espe.sistemamensajeria.exception.ResourceNotFoundException;
import com.espe.sistemamensajeria.repository.ClienteRepository;

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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void listarTodosDebeRetornarClientes() {
        when(clienteRepository.findAll())
                .thenReturn(List.of(new Cliente()));

        List<Cliente> resultado = clienteService.listarTodos();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorIdDebeRetornarCliente() {
        Cliente cliente = new Cliente();

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(cliente, resultado.get());
    }

    @Test
    void guardarDebeGuardarCliente() {
        Cliente cliente = new Cliente();

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.guardar(cliente);

        assertEquals(cliente, resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarDebeModificarClienteExistente() {
        Cliente cliente = new Cliente();
        Cliente actualizado = new Cliente();

        actualizado.setNombre("Juan Perez");
        actualizado.setCorreo("juan@gmail.com");
        actualizado.setTelefono("0998765432");
        actualizado.setDireccion("Quito");

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.actualizar(1L, actualizado);

        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("juan@gmail.com", resultado.getCorreo());
        assertEquals("0998765432", resultado.getTelefono());
        assertEquals("Quito", resultado.getDireccion());
    }

    @Test
    void eliminarDebeBorrarClienteExistente() {
        Cliente cliente = new Cliente();

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.eliminar(1L);

        verify(clienteRepository).delete(cliente);
    }

    @Test
    void actualizarDebeLanzarExcepcionSiClienteNoExiste() {
        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> clienteService.actualizar(999L, new Cliente())
        );
    }
}
