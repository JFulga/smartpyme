package com.tienda.smartP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductoRequestDTO {

    @NotBlank
    private String nombre;

    @Positive
    private Double precio;

    @Positive
    private Integer stock;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
}