package com.tienda.smartP.controller;

// 1. Importa tus propios modelos y el Service
import com.tienda.smartP.model.Factura;
import com.tienda.smartP.service.VentaService;

// 2. Imports de Spring Framework para crear la API
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tienda.smartP.dto.VentaRequestDTO;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin("*") // Para permitir peticiones desde tu frontend
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // --- CREATE ---

    @PostMapping
    public ResponseEntity<?> realizarVenta(@RequestBody VentaRequestDTO nuevaVenta) {
        try {
            Factura facturaProcesada = ventaService.crearVenta(nuevaVenta);
            return new ResponseEntity<>(facturaProcesada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // --- READ (TODAS) ---
    @GetMapping
    public ResponseEntity<List<Factura>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    // --- READ (POR ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Factura factura = ventaService.buscarPorId(id);
            return ResponseEntity.ok(factura);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // --- DELETE (ANULAR) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}