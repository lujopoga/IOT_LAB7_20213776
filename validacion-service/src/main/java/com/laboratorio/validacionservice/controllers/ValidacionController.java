package com.laboratorio.validacionservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validacion")
public class ValidacionController {

    @GetMapping("/dni/{dni}")
    public String validarDni(@PathVariable String dni) {
        System.out.println(">>> [VALIDACION-SERVICE] validarDni: " + dni);
        if (dni.length() == 8 && dni.matches("\\d+")) {
            return "DNI válido";
        }
        return "DNI inválido";
    }

    @GetMapping("/correo/{correo}")
    public String validarCorreo(@PathVariable String correo) {
        System.out.println(">>> [VALIDACION-SERVICE] validarCorreo: " + correo);
        if (correo.endsWith("@pucp.edu.pe")) {
            return "Correo válido";
        }
        return "Correo inválido";
    }
}
