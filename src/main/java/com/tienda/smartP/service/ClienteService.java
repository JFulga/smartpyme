package com.tienda.smartP.service;

import com.tienda.smartP.dto.ClienteDTO;
import com.tienda.smartP.model.Cliente;
import com.tienda.smartP.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepo;

    public ClienteDTO guardarCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCedula(dto.getCedula());
        cliente.setTelefono(dto.getTelefono());

        Cliente guardado = clienteRepo.save(cliente);

        return convertirADTO(guardado);
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepo.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setCedula(cliente.getCedula());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }
    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto) {

        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setCedula(dto.getCedula());
        cliente.setTelefono(dto.getTelefono());

        Cliente actualizado = clienteRepo.save(cliente);

        return convertirADTO(actualizado);
    }

    public void eliminarCliente(Long id) {

        Cliente cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        clienteRepo.delete(cliente);
    }
}