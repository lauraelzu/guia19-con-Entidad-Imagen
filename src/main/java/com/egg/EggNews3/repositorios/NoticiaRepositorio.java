package com.egg.EggNews3.repositorios;

import com.egg.EggNews3.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia,Long>{
    
    @Query("SELECT n FROM Noticia n ORDER BY n.fechaPublicacion DESC")
    public List<Noticia> ordenarPorFecha();
    
}
