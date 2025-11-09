package com.restaurante.saborgourmet.repository;

import com.restaurante.saborgourmet.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(String dni);
    List<Cliente> findByEstado(Cliente.EstadoCliente estado);
    boolean existsByDni(String dni);
}