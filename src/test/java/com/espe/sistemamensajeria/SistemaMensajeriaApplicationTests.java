
package com.espe.sistemamensajeria;

import com.espe.sistemamensajeria.entity.Cliente;
import com.espe.sistemamensajeria.repository.ClienteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SistemaMensajeriaApplicationTests {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void contextLoads() {
        assertNotNull(clienteRepository);
    }

    @Test
    void guardarClienteDebePersistirEnBaseDeDatos() {
        Cliente cliente = crearCliente();

        Cliente guardado = clienteRepository.save(cliente);

        assertNotNull(guardado.getId());
        assertTrue(clienteRepository.findById(guardado.getId()).isPresent());
    }

    @Test
    void consultarClienteDebeRetornarDatosCorrectos() {
        Cliente cliente = clienteRepository.save(crearCliente());

        Cliente encontrado = clienteRepository.findById(cliente.getId())
                .orElse(null);

        assertNotNull(encontrado);
        assertEquals("Cliente de prueba", encontrado.getNombre());
        assertEquals("prueba@gmail.com", encontrado.getCorreo());
    }

    @Test
    void actualizarClienteDebeModificarDatos() {
        Cliente cliente = clienteRepository.save(crearCliente());

        cliente.setNombre("Cliente actualizado");
        clienteRepository.save(cliente);

        Cliente actualizado = clienteRepository.findById(cliente.getId())
                .orElse(null);

        assertNotNull(actualizado);
        assertEquals("Cliente actualizado", actualizado.getNombre());
    }

    @Test
    void eliminarClienteDebeBorrarloDeBaseDeDatos() {
        Cliente cliente = clienteRepository.save(crearCliente());
        Long id = cliente.getId();

        clienteRepository.deleteById(id);

        assertFalse(clienteRepository.findById(id).isPresent());
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente de prueba");
        cliente.setCorreo("prueba@gmail.com");
        cliente.setTelefono("0991234567");
        cliente.setDireccion("Quito");
        return cliente;
    }
}
