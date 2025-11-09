package com.restaurante.saborgourmet.repository;

import com.restaurante.saborgourmet.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByNumero(String numero);
    List<Mesa> findByEstado(Mesa.EstadoMesa estado);
    boolean existsByNumero(String numero);
    List<Mesa> findByEstadoAndCapacidadGreaterThanEqual(Mesa.EstadoMesa estado, Integer capacidad);
}