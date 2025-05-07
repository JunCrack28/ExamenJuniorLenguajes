package com.examen.examen.Service;

import com.examen.examen.Exception.CapacidadExcedidaException;
import com.examen.examen.Model.Contenedor;
import com.examen.examen.Repository.BarcoRepository;
import com.examen.examen.Repository.ContenedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContenedorService {

    @Autowired
    private ContenedorRepository contenedorRepository;

    @Autowired
    private BarcoRepository barcoRepository;

    public List<Contenedor> findAll() {
        return contenedorRepository.findAll();
    }

    public List<Contenedor> findByBarcoId(Long barcoId) {
        return contenedorRepository.findByBarcoId(barcoId);
    }

    public Optional<Contenedor> findById(Long id) {
        return contenedorRepository.findById(id);
    }

    public Contenedor save(Contenedor contenedor) {
        Long barcoId = contenedor.getBarco().getId();
        List<Contenedor> contenedores = contenedorRepository.findByBarcoId(barcoId);
        Integer capacidad = barcoRepository.findById(barcoId)
                .orElseThrow(() -> new IllegalArgumentException("Barco no encontrado"))
                .getCapacidad();

        // Si es un nuevo contenedor (id es null), verificar la capacidad
        if (contenedor.getId() == null && contenedores.size() >= capacidad) {
            throw new CapacidadExcedidaException("No se puede agregar el contenedor: el barco ha alcanzado su capacidad máxima de " + capacidad + " contenedores.");
        }

        // Si el contenedor ya existe (id no es null), actualizarlo
        if (contenedor.getId() != null) {
            Optional<Contenedor> existingContenedor = contenedorRepository.findById(contenedor.getId());
            if (existingContenedor.isPresent()) {
                Contenedor updatedContenedor = existingContenedor.get();
                updatedContenedor.setVin(contenedor.getVin());
                updatedContenedor.setFecha(contenedor.getFecha());
                updatedContenedor.setBarco(contenedor.getBarco());
                return contenedorRepository.save(updatedContenedor);
            }
        }

        // Si es un nuevo contenedor, guardarlo
        return contenedorRepository.save(contenedor);
    }

    public Optional<Contenedor> update(Long id, Contenedor contenedorDetails) {
        Optional<Contenedor> contenedorOptional = contenedorRepository.findById(id);
        if (contenedorOptional.isPresent()) {
            Contenedor contenedor = contenedorOptional.get();
            contenedor.setVin(contenedorDetails.getVin());
            contenedor.setFecha(contenedorDetails.getFecha());
            contenedor.setBarco(contenedorDetails.getBarco());
            return Optional.of(contenedorRepository.save(contenedor));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (contenedorRepository.existsById(id)) {
            contenedorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}