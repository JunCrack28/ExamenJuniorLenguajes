package com.examen.examen.Repository;

import com.examen.examen.Model.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
    List<Contenedor> findByBarcoId(Long barcoId);
}