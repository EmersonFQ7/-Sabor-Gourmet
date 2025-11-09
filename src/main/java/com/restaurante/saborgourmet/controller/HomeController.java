package com.restaurante.saborgourmet.controller;

import com.restaurante.saborgourmet.service.ClienteService;
import com.restaurante.saborgourmet.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("titulo", "Dashboard - Sabor Gourmet");
        model.addAttribute("mesasDisponibles", mesaService.findMesasDisponibles().size());
        model.addAttribute("totalClientes", clienteService.findAll().size());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}