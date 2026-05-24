package com.tienda.smartP.service;

import com.tienda.smartP.dto.VentaRequestDTO;
import com.tienda.smartP.model.Cliente;
import com.tienda.smartP.model.Factura;
import com.tienda.smartP.model.FacturaDetalle;
import com.tienda.smartP.model.Producto;
import com.tienda.smartP.repository.ClienteRepository;
import com.tienda.smartP.repository.FacturaRepository;
import com.tienda.smartP.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private FacturaRepository facturaRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Transactional
    public Factura crearVenta(VentaRequestDTO ventaDTO) {

        Factura nuevaVenta = new Factura();

        Cliente cliente = clienteRepo.findById(ventaDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        nuevaVenta.setCliente(cliente);
        nuevaVenta.setFecha(LocalDateTime.now());

        List<FacturaDetalle> detalles = ventaDTO.getProductos().stream().map(dto -> {

            Producto producto = productoRepo.findById(dto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < dto.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock de: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - dto.getCantidad());
            productoRepo.save(producto);

            FacturaDetalle detalle = new FacturaDetalle();
            detalle.setProducto(producto);
            detalle.setCantidad(dto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setFactura(nuevaVenta);

            return detalle;
        }).toList();

        Double total = detalles.stream()
                .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                .sum();

        nuevaVenta.setDetalles(detalles);
        nuevaVenta.setTotal(total);

        return facturaRepo.save(nuevaVenta);
    }

    public List<Factura> listarTodas() {
        return facturaRepo.findAll();
    }

    public Factura buscarPorId(Long id) {
        return facturaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    @Transactional
    public void eliminar(Long id) {
        Factura factura = buscarPorId(id);

        factura.getDetalles().forEach(detalle -> {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepo.save(producto);
        });

        facturaRepo.delete(factura);
    }
}