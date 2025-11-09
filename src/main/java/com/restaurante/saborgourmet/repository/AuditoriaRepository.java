package com.restaurante.saborgourmet.repository;

import com.restaurante.saborgourmet.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByEntidadOrderByFechaRegistroDesc(String entidad);
    List<Auditoria> findByAccionOrderByFechaRegistroDesc(String accion);
}