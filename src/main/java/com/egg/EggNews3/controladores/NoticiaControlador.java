package com.egg.EggNews3.controladores;

import com.egg.EggNews3.entidades.Noticia;
import com.egg.EggNews3.entidades.Usuario;
import com.egg.EggNews3.excepciones.ErrorServicio;
import com.egg.EggNews3.servicios.NoticiaServicio;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {
    
    @Autowired
    private NoticiaServicio noticiaServicio;
      
    @GetMapping("/registrar")      //localhost:8080/noticia/registrar
    public String registrar(){
        return "noticia_formulario.html";
    }
    
    @PostMapping("/guardar")      
    public String guardar(@RequestParam String titulo,@RequestParam String cuerpo, MultipartFile archivo ,@RequestParam String dni, ModelMap modelo){
        try {
            noticiaServicio.crearNoticia(titulo, cuerpo, archivo, dni);
            modelo.put("exito","noticia publicada correctamente");
        } catch (ErrorServicio ex) {
            modelo.put("error",ex.getMessage());
            modelo.put("titulo", titulo);
            return "noticia_formulario.html";
        }
        return "redirect:/index";
    }
    
    
    
    @GetMapping("/mostrar/{id}")
    public String mostrarNoticia(@PathVariable Long id, ModelMap modelo){
        Noticia noticia = noticiaServicio.mostrarNoticia(id);
        modelo.addAttribute("noticia", noticia);
        return "noticia_completa.html";
    }
    
    @GetMapping("/editar")
    public String editarNoticia(ModelMap modelo, HttpSession sesion){
        List<Noticia> noticias = noticiaServicio.mostrarNoticiasOrdenadas();
        modelo.addAttribute("noticias",noticias);
        return "panelAdmin.html";
    }
    
    //muestra la noticia según su id
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')") //
    @GetMapping("/modificar/{id}")                                           //
    public String modificarNoticia(@PathVariable Long id, ModelMap modelo, HttpSession sesion){
        Usuario usuario = (Usuario) sesion.getAttribute("usuariosesion");//
        modelo.put("usuario", usuario);  //
        modelo.put("noticia", noticiaServicio.obtenerNoticia(id));
        return "noticia_modificar.html";
    }
    
    //edita la noticia específica (título, cuerpo, imagen)
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")  //
    @PostMapping("/modificar/{id}")
    public String modificarNoticia(@PathVariable Long id, String titulo, String cuerpo, MultipartFile archivo, ModelMap modelo){
        try {
            noticiaServicio.modificarNoticia(id, titulo, cuerpo, archivo);
            modelo.put("exito", "Noticia actualizada correctamente");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("cuerpo", cuerpo);
            return "noticia_modificar.html";
        } 
        List<Noticia> noticias = noticiaServicio.mostrarNoticiasOrdenadas();
        modelo.addAttribute("noticias",noticias);
        return "panelAdmin.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarNoticia(@PathVariable Long id, ModelMap modelo){
        noticiaServicio.eliminarNoticia(id);
        List<Noticia> noticias = noticiaServicio.mostrarNoticiasOrdenadas();
        modelo.addAttribute("noticias",noticias);
        return "panelAdmin.html";
    }
    
}
