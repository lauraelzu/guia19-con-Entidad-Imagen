package com.egg.EggNews3.servicios;

import com.egg.EggNews3.entidades.Periodista;
import com.egg.EggNews3.enumeraciones.Rol;
import com.egg.EggNews3.excepciones.ErrorServicio;
import com.egg.EggNews3.repositorios.PeriodistaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PeriodistaServicio{
    
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    
   
    @Transactional
    public void crearPeriodista(String dni, String nombre, String clave, String clave2, float sueldoMensual) throws ErrorServicio{
        
        validar(dni, nombre, clave, clave2);
        
        Periodista periodista = new Periodista();
        periodista.setDni(dni);
        periodista.setNombre(nombre);
        periodista.setClave(new BCryptPasswordEncoder().encode(clave));
        periodista.setFechaAlta(new Date());
        periodista.setCantidadNoticias(0);
        periodista.setFechaBaja(null);
        periodista.setSueldoMensual(sueldoMensual);
        
        periodista.setRol(Rol.PERIODISTA);
        
        periodistaRepositorio.save(periodista);
    }
    
    @Transactional
    public void modificarPeriodista(String dni, String nombre, String clave, String clave2) throws ErrorServicio{
        
        validar(dni, nombre, clave, clave2);
        
        //AUTENTICACION por dni
        Optional<Periodista> respuesta = periodistaRepositorio.findById(dni);
        
        if(respuesta.isPresent()){
            Periodista periodista = respuesta.get();
            periodista.setNombre(nombre);
            periodista.setClave(new BCryptPasswordEncoder().encode(clave));
            periodistaRepositorio.save(periodista);
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
    
    
    public List<Periodista> mostrarPeriodistas(){
       // List<Periodista> periodistas = periodistaRepositorio.findAll(); //muestra TODOS los periodistas
        List<Periodista> periodistas = periodistaRepositorio.mostrarPeriodistasActivos();
            return periodistas;
    }
    
 
    @Transactional
    public void eliminar(String dni){
        Optional<Periodista> respuesta = periodistaRepositorio.findById(dni);
        if(respuesta.isPresent()){
             Periodista periodista = respuesta.get();
             periodista.setFechaBaja(new Date());
             periodistaRepositorio.save(periodista);
        }
    }
    
}
