package com.egg.EggNews3.controladores;

import com.egg.EggNews3.entidades.Noticia;
import com.egg.EggNews3.entidades.Periodista;
import com.egg.EggNews3.excepciones.ErrorServicio;
import com.egg.EggNews3.servicios.NoticiaServicio;
import com.egg.EggNews3.servicios.PeriodistaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private PeriodistaServicio periodistaServicio;
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    @GetMapping("/dashboard")
    public String administrar(ModelMap modelo){
         List<Noticia> noticias = noticiaServicio.mostrarNoticiasOrdenadas();
         modelo.addAttribute("noticias",noticias);
        return "panelAdmin.html";
    }
    
    @GetMapping("/registrar")   
    public String registrar(){
        return "periodista_formulario.html";
    }
    
    @PostMapping("/guardar")
                         //atributos con el mismo nombre que las variables de los input de periodista_formulario.html
    public String guardar(@RequestParam String dni, @RequestParam String nombre, @RequestParam String clave,@RequestParam String clave2,@RequestParam float sueldoMensual, ModelMap modelo){ 

        try {
            periodistaServicio.crearPeriodista(dni, nombre, clave, clave2, sueldoMensual);
            modelo.put("exito","periodista creado correctamente");
            return "panelAdmin.html";    
        } catch (ErrorServicio ex) {
            modelo.put("error",ex.getMessage());
            modelo.put("dni", dni);  //en caso de error que rellene con los datos ya ingresados
            modelo.put("nombre", nombre);
            modelo.put("sueldoMensual", sueldoMensual);
            
            return "periodista_formulario.html";
        }        
    }
    
    
    @GetMapping("/listar")
    public String listar(ModelMap modelo){
        List<Periodista> periodistas = periodistaServicio.mostrarPeriodistas();
        modelo.addAttribute("periodistas",periodistas);
        return "periodista_listado.html";
    }
    
    
    @GetMapping("/eliminar/{dni}")
    public String eliminarPeriodista(@PathVariable String dni, ModelMap modelo){
        periodistaServicio.eliminar(dni);     // no se puede usar deleteById => SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`eggnews`.`noticia`, CONSTRAINT `FKe3xqmj9irkycyo3t026p3awlp` FOREIGN KEY (`creador_dni`) REFERENCES `usuario` (`dni`))
        return "redirect:/admin/dashboard";
    }
}
