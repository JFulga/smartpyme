package com.tienda.smartP.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String categoria;
}