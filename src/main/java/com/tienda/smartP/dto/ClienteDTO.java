package com.tienda.smartP.dto;

import lombok.Data;

@Data // Si usas Lombok, si no, genera Getters y Setters manualmente
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private int cedula;
    private String telefono;
}