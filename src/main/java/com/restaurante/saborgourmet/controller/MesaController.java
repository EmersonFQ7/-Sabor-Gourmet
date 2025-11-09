package com.restaurante.saborgourmet.controller;

import com.restaurante.saborgourmet.model.Mesa;
import com.restaurante.saborgourmet.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public String listarMesas(Model model) {
        model.addAttribute("mesas", mesaService.findAll());
        model.addAttribute("titulo", "Gestión de Mesas");
        return "mesas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("mesa", new Mesa());
        model.addAttribute("titulo", "Nueva Mesa");
        return "mesas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMesa(@Valid @ModelAttribute Mesa mesa,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo",
                    mesa.getIdMesa() != null ? "Editar Mesa" : "Nueva Mesa");
            return "mesas/formulario";
        }

        mesaService.save(mesa);
        redirectAttributes.addFlashAttribute("success",
                mesa.getIdMesa() != null ?
                        "Mesa actualizada correctamente" : "Mesa registrada correctamente");

        return "redirect:/mesas";
    }

    @GetMapping("/editar/{id}")
    public String editarMesa(@PathVariable Long id, Model model,
                             RedirectAttributes redirectAttributes) {
        Optional<Mesa> mesa = mesaService.findById(id);
        if (mesa.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mesa no encontrada");
            return "redirect:/mesas";
        }

        model.addAttribute("mesa", mesa.get());
        model.addAttribute("titulo", "Editar Mesa");
        return "mesas/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (mesaService.findById(id).isPresent()) {
            mesaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Mesa eliminada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mesa no encontrada");
        }
        return "redirect:/mesas";
    }

    @GetMapping("/ocupar/{id}")
    public String ocuparMesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (mesaService.ocuparMesa(id)) {
            redirectAttributes.addFlashAttribute("success", "Mesa ocupada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo ocupar la mesa");
        }
        return "redirect:/mesas";
    }

    @GetMapping("/liberar/{id}")
    public String liberarMesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (mesaService.liberarMesa(id)) {
            redirectAttributes.addFlashAttribute("success", "Mesa liberada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo liberar la mesa");
        }
        return "redirect:/mesas";
    }

    @GetMapping("/disponibles")
    public String mesasDisponibles(Model model) {
        model.addAttribute("mesas", mesaService.findMesasDisponibles());
        model.addAttribute("titulo", "Mesas Disponibles");
        return "mesas/disponibles";
    }
}