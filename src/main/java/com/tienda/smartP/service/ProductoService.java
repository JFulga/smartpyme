package com.tienda.smartP.service;

import com.tienda.smartP.dto.ProductoRequestDTO;
import com.tienda.smartP.dto.ProductoResponseDTO;
import com.tienda.smartP.exception.BusinessException;
import com.tienda.smartP.model.Producto;
import com.tienda.smartP.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepo;

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepo.findByActivoTrue()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Transactional
    public ProductoResponseDTO guardar(ProductoRequestDTO dto) {

        if (productoRepo.existsByNombre(dto.getNombre())) {
            throw new BusinessException(
                    "El nombre del producto '" + dto.getNombre() + "' ya existe"
            );
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecioVenta(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());

        Producto guardado = productoRepo.save(producto);

        return convertirAResponseDTO(guardado);
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {

        Producto productoExistente = productoRepo.findById(id)
                .orElseThrow(() ->
                        new BusinessException("Producto no encontrado con ID: " + id));

        if (productoRepo.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new BusinessException(
                    "El nombre '" + dto.getNombre() + "' ya está siendo usado por otro producto"
            );
        }

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setPrecioVenta(dto.getPrecio());
        productoExistente.setStock(dto.getStock());
        productoExistente.setCategoria(dto.getCategoria());


        Producto actualizado = productoRepo.save(productoExistente);

        return convertirAResponseDTO(actualizado);
    }



    public void eliminar(Long id) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setActivo(false);
        productoRepo.save(producto);
    }

    public void restaurar(Long id) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setActivo(true);
        productoRepo.save(producto);
    }

    public List<ProductoResponseDTO> listarInactivos() {
        return productoRepo.findByActivoFalse()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .precio(producto.getPrecioVenta())
                .stock(producto.getStock())
                .categoria(producto.getCategoria())

                .build();
    }
}