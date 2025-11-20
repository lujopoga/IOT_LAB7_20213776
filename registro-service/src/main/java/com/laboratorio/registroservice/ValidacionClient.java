package com.laboratorio.registroservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "validacion-service")
public interface ValidacionClient {

    @GetMapping("/validacion/dni/{dni}")
    String validarDni(@PathVariable String dni);

    @GetMapping("/validacion/correo/{correo}")
    String validarCorreo(@PathVariable String correo);
}
