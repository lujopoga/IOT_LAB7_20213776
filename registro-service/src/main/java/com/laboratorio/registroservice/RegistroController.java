package com.laboratorio.registroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistroController {

    @Autowired
    private ValidacionClient validacionClient;


    @PostMapping("/registro")
    public ResponseEntity<String> registerUser(@RequestBody Usuario usuario) {

        System.out.println(">>> [REGISTRO-SERVICE] /registro llamado con: dni="
                + usuario.getDni() + ", correo=" + usuario.getCorreo());

        String validacionDni = validacionClient.validarDni(usuario.getDni());
        String validacionCorreo = validacionClient.validarCorreo(usuario.getCorreo());

        System.out.println(">>> [REGISTRO-SERVICE] Resultado DNI: " + validacionDni);
        System.out.println(">>> [REGISTRO-SERVICE] Resultado Correo: " + validacionCorreo);

        if ("DNI válido".equals(validacionDni) && "Correo válido".equals(validacionCorreo)) {
            System.out.println(">>> [REGISTRO-SERVICE] Registro exitoso");
            return ResponseEntity.ok("Registro exitoso");
        }

        String msg = "Validación fallida: " +
                (validacionDni.equals("DNI inválido") ? "DNI inválido " : "") +
                (validacionCorreo.equals("Correo inválido") ? "Correo inválido" : "");

        System.out.println(">>> [REGISTRO-SERVICE] " + msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
