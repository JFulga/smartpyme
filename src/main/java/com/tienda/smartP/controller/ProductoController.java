package com.tienda.smartP.controller;

import com.tienda.smartP.dto.ProductoRequestDTO;
import com.tienda.smartP.dto.ProductoResponseDTO;
import com.tienda.smartP.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardar(
            @Valid @RequestBody ProductoRequestDTO request
    ) {
        return new ResponseEntity<>(
                productoService.guardar(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return productoService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request
    ) {
        return ResponseEntity.ok(
                productoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    @PutMapping("/restaurar/{id}")
    public ResponseEntity<String> restaurar(@PathVariable Long id) {
        productoService.restaurar(id);
        return ResponseEntity.ok("Producto restaurado");
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<ProductoResponseDTO>> listarInactivos() {
        return ResponseEntity.ok(productoService.listarInactivos());
    }
}