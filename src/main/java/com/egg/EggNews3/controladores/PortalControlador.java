package com.egg.EggNews3.controladores;

import com.egg.EggNews3.entidades.Noticia;
import com.egg.EggNews3.entidades.Usuario;
import com.egg.EggNews3.servicios.NoticiaServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")       //localhost:8080/
public class PortalControlador {
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    //cualquiera puede acceder a inicio
    @GetMapping("/")       //localhost:8080/
    public String inicio(ModelMap modelo){
        return "inicio.html";
    }
    
    //error en caso de ingresar mal las credenciales... 
    @GetMapping("/login")       
    public String login(@RequestParam(required = false) String error, ModelMap modelo){ 
        if(error != null){
            modelo.put("error", "Usuario o contraseña inválidos");
        }
        return "login.html";
    }
    
      //sólo los roles logueados como USER, PERIODISTA y ADMIN pueden acceder a la vista index
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PERIODISTA','ROLE_ADMIN')")
    @GetMapping("/index")       
    public String index(HttpSession sesion, ModelMap modelo){   
        Usuario logueado = (Usuario) sesion.getAttribute("usuariosesion");
        if(logueado.getRol().toString().equals("ADMIN")){
            return "redirect:/admin/dashboard";
        }

        List<Noticia> noticias = noticiaServicio.mostrarNoticiasOrdenadas();
        modelo.addAttribute("noticias",noticias);
        return "index.html";
    }
    
}
