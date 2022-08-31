package com.egg.EggNews3.controladores;

import com.egg.EggNews3.excepciones.ErrorServicio;
import com.egg.EggNews3.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")     //localhost:8080/usuario
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/registrar")   //localhost:8080/usuario/registrar
    public String registrar(){
        return "usuario_formulario.html";
    }
    
    @PostMapping("/guardar")
                         //atributos con el mismo nombre que las variables de los input de usuario_formulario.html
    public String guardar(@RequestParam String dni, @RequestParam String nombre, @RequestParam String clave, @RequestParam String clave2, ModelMap modelo){ 

        try {
            usuarioServicio.crearUsuario(dni, nombre, clave, clave2);
            modelo.put("exito","usuario creado correctamente");
            return "inicio.html";    
        } catch (ErrorServicio ex) {
            modelo.put("error",ex.getMessage());
            modelo.put("dni", dni);  //en caso de error que rellene con los datos ya ingresados
            modelo.put("nombre", nombre);
            
            return "usuario_formulario.html";
        }        
    }
}
