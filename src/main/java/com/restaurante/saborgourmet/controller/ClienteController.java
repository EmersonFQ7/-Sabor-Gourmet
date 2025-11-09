package com.restaurante.saborgourmet.controller;

import com.restaurante.saborgourmet.model.Cliente;
import com.restaurante.saborgourmet.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("titulo", "Gestión de Clientes");
        return "clientes/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo Cliente");
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute Cliente cliente,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("titulo",
                    cliente.getIdCliente() != null ? "Editar Cliente" : "Nuevo Cliente");
            return "clientes/formulario";
        }

        // Validar DNI único
        if (cliente.getIdCliente() == null) {
            if (clienteService.existsByDni(cliente.getDni())) {
                result.rejectValue("dni", "error.cliente", "El DNI ya está registrado");
                model.addAttribute("titulo", "Nuevo Cliente");
                return "clientes/formulario";
            }
        }

        clienteService.save(cliente);
        redirectAttributes.addFlashAttribute("success",
                cliente.getIdCliente() != null ?
                        "Cliente actualizado correctamente" : "Cliente registrado correctamente");

        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
            return "redirect:/clientes";
        }

        model.addAttribute("cliente", cliente.get());
        model.addAttribute("titulo", "Editar Cliente");
        return "clientes/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (clienteService.findById(id).isPresent()) {
            clienteService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
        }
        return "redirect:/clientes";
    }
}