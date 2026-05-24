package com.tienda.smartP.controller;

import com.tienda.smartP.dto.ClienteDTO;
import com.tienda.smartP.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ClienteDTO guardar(@RequestBody ClienteDTO dto) {
        return clienteService.guardarCliente(dto);
    }

    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteService.listarTodos();
    }
    @PutMapping("/{id}")
    public ClienteDTO actualizar(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        return clienteService.actualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }
}