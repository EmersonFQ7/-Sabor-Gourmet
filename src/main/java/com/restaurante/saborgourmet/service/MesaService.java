package com.restaurante.saborgourmet.service;

import com.restaurante.saborgourmet.model.Mesa;
import com.restaurante.saborgourmet.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> findById(Long id) {
        return mesaRepository.findById(id);
    }

    public Mesa save(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public void deleteById(Long id) {
        mesaRepository.deleteById(id);
    }

    public List<Mesa> findByEstado(Mesa.EstadoMesa estado) {
        return mesaRepository.findByEstado(estado);
    }

    public List<Mesa> findMesasDisponibles() {
        return mesaRepository.findByEstado(Mesa.EstadoMesa.DISPONIBLE);
    }

    public List<Mesa> findMesasDisponiblesConCapacidad(Integer capacidad) {
        return mesaRepository.findByEstadoAndCapacidadGreaterThanEqual(
                Mesa.EstadoMesa.DISPONIBLE, capacidad);
    }

    public boolean ocuparMesa(Long idMesa) {
        Optional<Mesa> mesaOpt = findById(idMesa);
        if (mesaOpt.isPresent() && mesaOpt.get().getEstado() == Mesa.EstadoMesa.DISPONIBLE) {
            Mesa mesa = mesaOpt.get();
            mesa.setEstado(Mesa.EstadoMesa.OCUPADA);
            save(mesa);
            return true;
        }
        return false;
    }

    public boolean liberarMesa(Long idMesa) {
        Optional<Mesa> mesaOpt = findById(idMesa);
        if (mesaOpt.isPresent() && mesaOpt.get().getEstado() == Mesa.EstadoMesa.OCUPADA) {
            Mesa mesa = mesaOpt.get();
            mesa.setEstado(Mesa.EstadoMesa.DISPONIBLE);
            save(mesa);
            return true;
        }
        return false;
    }
}