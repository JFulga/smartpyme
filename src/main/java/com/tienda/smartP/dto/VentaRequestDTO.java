package com.tienda.smartP.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private Long clienteId;
    private List<ProductoVentaDTO> productos;
}