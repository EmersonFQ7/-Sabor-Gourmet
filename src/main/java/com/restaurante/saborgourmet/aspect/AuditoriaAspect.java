package com.restaurante.saborgourmet.aspect;

import com.restaurante.saborgourmet.model.Auditoria;
import com.restaurante.saborgourmet.repository.AuditoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* com.restaurante.saborgourmet.service.ClienteService.save(..))")
    public void clienteSave() {}

    @Pointcut("execution(* com.restaurante.saborgourmet.service.ClienteService.deleteById(..))")
    public void clienteDelete() {}

    @Pointcut("execution(* com.restaurante.saborgourmet.service.MesaService.save(..))")
    public void mesaSave() {}

    @Pointcut("execution(* com.restaurante.saborgourmet.service.MesaService.deleteById(..))")
    public void mesaDelete() {}

    @AfterReturning(pointcut = "clienteSave() || mesaSave()", returning = "result")
    public void logAfterSave(JoinPoint joinPoint, Object result) {
        try {
            Object entity = result;
            String accion = "CREATE";
            String entidad = entity.getClass().getSimpleName();
            Long idEntidad = obtenerIdEntidad(entity);

            // Verificar si es update (si el ID ya existe)
            if (idEntidad != null && idEntidad > 0) {
                accion = "UPDATE";
            }

            String datosNuevos = objectMapper.writeValueAsString(entity);

            registrarAuditoria(accion, entidad, idEntidad, null, datosNuevos);

        } catch (Exception e) {
            System.err.println("Error en auditoría de save: " + e.getMessage());
        }
    }

    @AfterReturning(pointcut = "clienteDelete() || mesaDelete()", returning = "result")
    public void logAfterDelete(JoinPoint joinPoint, Object result) {
        try {
            Long idEntidad = (Long) joinPoint.getArgs()[0];
            String entidad = joinPoint.getTarget().getClass().getSimpleName()
                    .replace("Service", "");

            registrarAuditoria("DELETE", entidad, idEntidad, "Registro eliminado", null);

        } catch (Exception e) {
            System.err.println("Error en auditoría de delete: " + e.getMessage());
        }
    }

    private Long obtenerIdEntidad(Object entity) {
        try {
            if (entity instanceof com.restaurante.saborgourmet.model.Cliente) {
                return ((com.restaurante.saborgourmet.model.Cliente) entity).getIdCliente();
            } else if (entity instanceof com.restaurante.saborgourmet.model.Mesa) {
                return ((com.restaurante.saborgourmet.model.Mesa) entity).getIdMesa();
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo ID de entidad: " + e.getMessage());
        }
        return null;
    }

    private void registrarAuditoria(String accion, String entidad, Long idEntidad,
                                    String datosAnteriores, String datosNuevos) {
        try {
            Auditoria auditoria = new Auditoria();
            auditoria.setAccion(accion);
            auditoria.setEntidad(entidad);
            auditoria.setIdEntidad(idEntidad);
            auditoria.setDatosAnteriores(datosAnteriores);
            auditoria.setDatosNuevos(datosNuevos);
            auditoria.setFechaRegistro(LocalDateTime.now());

            // Obtener usuario autenticado
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            auditoria.setUsuario(username);

            // Obtener IP
            String ip = obtenerIpCliente();
            auditoria.setIp(ip);

            auditoriaRepository.save(auditoria);

        } catch (Exception e) {
            System.err.println("Error registrando auditoría: " + e.getMessage());
        }
    }

    private String obtenerIpCliente() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                        .map(ips -> ips.split(",")[0])
                        .orElse(request.getRemoteAddr());
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo IP: " + e.getMessage());
        }
        return "127.0.0.1";
    }
}