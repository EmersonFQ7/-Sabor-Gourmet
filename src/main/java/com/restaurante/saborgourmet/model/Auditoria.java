package com.restaurante.saborgourmet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accion; // CREATE, UPDATE, DELETE

    @Column(nullable = false)
    private String entidad; // Cliente, Mesa, etc.

    private Long idEntidad;

    @Column(columnDefinition = "TEXT")
    private String datosAnteriores;

    @Column(columnDefinition = "TEXT")
    private String datosNuevos;

    private String usuario;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    private String ip;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}