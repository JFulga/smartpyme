package com.tienda.smartP.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "facturas")
@Getter
@Setter
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // --- EL CAMBIO ESTÁ AQUÍ ---
    // Quitamos @JoinColumn y usamos mappedBy
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FacturaDetalle> detalles;
    // ----------------------------

    private Double total;
}