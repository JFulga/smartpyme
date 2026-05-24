package com.tienda.smartP.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FacturaResponseDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private List<DetalleResumenDTO> detalles;

    @Data
    public static class DetalleResumenDTO {
        private String nombreProducto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
    }
}