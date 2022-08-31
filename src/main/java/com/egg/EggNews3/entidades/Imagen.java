package com.egg.EggNews3.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Imagen {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    //atributo MIME contendrá el FORMATO del archivo de la imagen (se obtiene con el metodo getContentType de MultipartFile)
    private String mime;
    private String nombre;
    //arreglo de bytes con el contenido de la imagen
    //@Lob informa a Spring que este archivo puede ser grande
    //@Basic informa el tipo de carga, LAZY perezosa o EAGER
    //LAZY el contenido solo se va a cargar cuando se pida con un getContenido, haciendo las query mas livianas
    //el resto de atributos por defecto es EAGER, se cargan al llamar al objeto
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

    public Imagen() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    
}
