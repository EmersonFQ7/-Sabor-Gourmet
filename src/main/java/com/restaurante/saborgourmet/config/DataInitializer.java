package com.restaurante.saborgourmet.config;

import com.restaurante.saborgourmet.model.Cliente;
import com.restaurante.saborgourmet.model.Mesa;
import com.restaurante.saborgourmet.model.Usuario;
import com.restaurante.saborgourmet.repository.ClienteRepository;
import com.restaurante.saborgourmet.repository.MesaRepository;
import com.restaurante.saborgourmet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios por defecto
        if (usuarioRepository.count() == 0) {
            crearUsuarios();
        }

        // Crear clientes de ejemplo
        if (clienteRepository.count() == 0) {
            crearClientes();
        }

        // Crear mesas de ejemplo
        if (mesaRepository.count() == 0) {
            crearMesas();
        }
    }

    private void crearUsuarios() {
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setNombreCompleto("Administrador del Sistema");
        admin.setRoles(Arrays.asList(Usuario.Rol.ADMIN));
        usuarioRepository.save(admin);

        Usuario mozo = new Usuario();
        mozo.setUsername("mozo");
        mozo.setPassword(passwordEncoder.encode("mozo"));
        mozo.setNombreCompleto("Mozo Principal");
        mozo.setRoles(Arrays.asList(Usuario.Rol.MOZO));
        usuarioRepository.save(mozo);

        Usuario cocinero = new Usuario();
        cocinero.setUsername("cocinero");
        cocinero.setPassword(passwordEncoder.encode("cocinero"));
        cocinero.setNombreCompleto("Chef Principal");
        cocinero.setRoles(Arrays.asList(Usuario.Rol.COCINERO));
        usuarioRepository.save(cocinero);

        Usuario cajero = new Usuario();
        cajero.setUsername("cajero");
        cajero.setPassword(passwordEncoder.encode("cajero"));
        cajero.setNombreCompleto("Cajero Principal");
        cajero.setRoles(Arrays.asList(Usuario.Rol.CAJERO));
        usuarioRepository.save(cajero);
    }

    private void crearClientes() {
        Cliente cliente1 = new Cliente();
        cliente1.setDni("12345678");
        cliente1.setNombres("Juan");
        cliente1.setApellidos("Pérez García");
        cliente1.setTelefono("987654321");
        cliente1.setCorreo("juan.perez@email.com");
        clienteRepository.save(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321");
        cliente2.setNombres("María");
        cliente2.setApellidos("López Mendoza");
        cliente2.setTelefono("912345678");
        cliente2.setCorreo("maria.lopez@email.com");
        clienteRepository.save(cliente2);
    }

    private void crearMesas() {
        for (int i = 1; i <= 10; i++) {
            Mesa mesa = new Mesa();
            mesa.setNumero("M" + i);
            mesa.setCapacidad(i % 3 == 0 ? 6 : i % 2 == 0 ? 4 : 2);
            mesaRepository.save(mesa);
        }
    }
}