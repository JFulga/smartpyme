package com.tienda.smartP.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // Importante
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "factura_detalles")
@Getter
@Setter
public class FacturaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    @JsonIgnore // <--- ESTO evita el bucle infinito en el JSON
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;
    private Double precioUnitario;
}