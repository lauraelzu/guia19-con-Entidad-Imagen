package com.egg.EggNews3.controladores;

import com.egg.EggNews3.entidades.Noticia;
import com.egg.EggNews3.servicios.NoticiaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    //ResponseEntity es la respuesta Http: formado por: contenido en bytes de la imagen, formato de la imagen y estado 200(éxito)
    //este método devuelve la imagen que corresponde a la id de una noticia en lugar de una vista o redirección a un controlador
    @GetMapping("/noticia/{id}")
    public ResponseEntity<byte[]> obtenerImagenNoticia (@PathVariable Long id){
        
        //traer la noticia que corresponda a esa id
        Noticia noticia = noticiaServicio.obtenerNoticia(id);
        
        //obtener el contenido en bytes que es lo que el navegador tiene que descargar
        //las imagenes en HTML se consumen como una URL:
        //el navegador interpreta el arreglo de bytes, lo coloca donde debe estar, con su tamaño, etc
        byte[] imagen = noticia.getImagen().getContenido();
        
        //cabecera indica al navegador que lo que devuelve es una imagen
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.setContentType(MediaType.IMAGE_JPEG); //setea que es imagen formato jpeg
        
        //HttpStatus es el código del estado con que termina el proceso
        //.OK es el estado 200
        return new ResponseEntity<>(imagen, cabecera, HttpStatus.OK);
    }
}
