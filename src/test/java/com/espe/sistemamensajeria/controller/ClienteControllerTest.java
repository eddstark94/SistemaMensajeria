
package com.espe.sistemamensajeria.controller;

import com.espe.sistemamensajeria.entity.Cliente;
import com.espe.sistemamensajeria.service.ClienteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void crearClienteDebeRetornar201() throws Exception {

        Cliente cliente = crearCliente();

        when(clienteService.guardar(any(Cliente.class)))
                .thenReturn(cliente);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Juan Perez",
                                  "correo": "juan@gmail.com",
                                  "telefono": "0998765432",
                                  "direccion": "Quito"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void listarClientesDebeRetornar200() throws Exception {

        when(clienteService.listarTodos())
                .thenReturn(List.of(crearCliente()));

        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }

    @Test
    void obtenerClienteDebeRetornar200() throws Exception {

        when(clienteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearCliente()));

        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizarClienteDebeRetornar200() throws Exception {

        Cliente actualizado = crearCliente();
        actualizado.setNombre("Juan Actualizado");

        when(clienteService.actualizar(eq(1L), any(Cliente.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Juan Actualizado",
                                  "correo": "juan@gmail.com",
                                  "telefono": "0998765432",
                                  "direccion": "Quito"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"));
    }

    @Test
    void eliminarClienteDebeRetornar204() throws Exception {

        when(clienteService.buscarPorId(1L))
                .thenReturn(Optional.of(crearCliente()));

        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).eliminar(1L);
    }

    @Test
    void crearClienteConDatosInvalidosDebeRetornar400() throws Exception {

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "correo": "correo-invalido",
                                  "telefono": "",
                                  "direccion": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(clienteService, never()).guardar(any(Cliente.class));
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");
        cliente.setCorreo("juan@gmail.com");
        cliente.setTelefono("0998765432");
        cliente.setDireccion("Quito");
        return cliente;
    }
}
