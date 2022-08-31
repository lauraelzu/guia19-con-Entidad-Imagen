//Periodista extiende de Usuario.
//Periodista tendrá como atributo extra cantidadNoticias(ArrayList) y sueldoMensual.
package com.egg.EggNews3.entidades;

import javax.persistence.Entity;

@Entity
public class Periodista extends Usuario{
    private float sueldoMensual;
    
    private int cantidadNoticias;

    public Periodista() {
        super();
    }

    public float getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(float sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }

    public int getCantidadNoticias() {
        return cantidadNoticias;
    }

    public void setCantidadNoticias(int cantidadNoticias) {
        this.cantidadNoticias = cantidadNoticias;
    }
    
    
    
}
