package com.egg.EggNews3.servicios;

import com.egg.EggNews3.entidades.Usuario;
import com.egg.EggNews3.enumeraciones.Rol;
import com.egg.EggNews3.excepciones.ErrorServicio;
import com.egg.EggNews3.repositorios.PeriodistaRepositorio;
import com.egg.EggNews3.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;                 //
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService{ //implementa Userdetailservice para AUTENTICAR usuarios/admin/periodistas
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    
    @Transactional
    public void crearUsuario(String dni, String nombre, String clave, String clave2) throws ErrorServicio{
        
        validar(dni, nombre, clave, clave2);
        
        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setClave(new BCryptPasswordEncoder().encode(clave)); //encriptar la clave
        usuario.setFechaAlta(new Date());
        usuario.setFechaBaja(null);
        
        usuario.setRol(Rol.USER);
        
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void modificarUsuario(String dni, String nombre, String clave, String clave2) throws ErrorServicio{
        
        validar(dni, nombre, clave, clave2);
        
        //AUTENTICACION por dni
        Optional<Usuario> respuesta = usuarioRepositorio.findById(dni);
        
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setClave(new BCryptPasswordEncoder().encode(clave));
            usuarioRepositorio.save(usuario);
        }      
    }
    
    public void validar(String dni, String nombre, String clave, String clave2) throws ErrorServicio{
        
        if(dni == null || dni.isEmpty()){
            throw new ErrorServicio("Debe indicar su DNI como identificador de usuario");
        }
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre no puede ser nulo o vacío");
        }
         if(clave == null || clave.isEmpty() || clave.length()<=5){
            throw new ErrorServicio("La clave no puede ser nula, vacía, ni menor a 6 caracteres");
        }
        if(!clave.equals(clave2)){
            throw new ErrorServicio("Las claves deben coincidir");
        }
    }
    
    //************************************************************************************************
    //****************AUTENTICACION - AUTORIZACION a través del dni***********************************
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
      
       Optional<Usuario> resptaUsr = usuarioRepositorio.findById(dni);
       if (resptaUsr.isPresent()){
           Usuario usuario = resptaUsr.get();
           
           List<GrantedAuthority> permisos = new ArrayList();
           GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //ROLE_USER || ROLE_ADMIN||USER_PERIODISTA          
           permisos.add(p);
           
           //recuperar información de la sesión del usuario logueado
           ServletRequestAttributes atr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
           HttpSession sesion = atr.getRequest().getSession(true);
           sesion.setAttribute("usuariosesion", usuario);
           
           return new User(usuario.getDni(), usuario.getClave(), permisos);
       } else {
           return null; //en caso que no haya encontrado      
        } 
          
    } 
}
