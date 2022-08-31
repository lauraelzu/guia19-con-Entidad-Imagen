package com.egg.EggNews3.repositorios;

import com.egg.EggNews3.entidades.Periodista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PeriodistaRepositorio extends JpaRepository<Periodista, String>{
    
    @Query("SELECT p FROM Periodista p WHERE p.fechaBaja IS NULL")
    public List<Periodista> mostrarPeriodistasActivos();
    
}
