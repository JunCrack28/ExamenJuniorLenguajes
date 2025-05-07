package com.examen.examen.Service;

import com.examen.examen.Exception.BarcoConContenedoresException;
import com.examen.examen.Exception.CapacidadExcedidaException;
import com.examen.examen.Model.Barco;
import com.examen.examen.Model.Contenedor;
import com.examen.examen.Repository.BarcoRepository;
import com.examen.examen.Repository.ContenedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarcoService {

    @Autowired
    private BarcoRepository barcoRepository;

    @Autowired
    private ContenedorRepository contenedorRepository;

    public List<Barco> findAll() {
        return barcoRepository.findAll();
    }

    public Optional<Barco> findById(Long id) {
        return barcoRepository.findById(id);
    }

    public Barco save(Barco barco) {
        return barcoRepository.save(barco);
    }

    public Optional<Barco> update(Long id, Barco barcoDetails) {
        Optional<Barco> barcoOptional = barcoRepository.findById(id);
        if (barcoOptional.isPresent()) {
            Barco barco = barcoOptional.get();
            List<Contenedor> contenedores = contenedorRepository.findByBarcoId(id);
            if (barcoDetails.getCapacidad() < contenedores.size()) {
                throw new CapacidadExcedidaException("No se puede reducir la capacidad a " + barcoDetails.getCapacidad() + " porque el barco ya tiene " + contenedores.size() + " contenedores asociados.");
            }
            barco.setNombre(barcoDetails.getNombre());
            barco.setOrigen(barcoDetails.getOrigen());
            barco.setDestino(barcoDetails.getDestino());
            barco.setCapacidad(barcoDetails.getCapacidad());
            return Optional.of(barcoRepository.save(barco));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (barcoRepository.existsById(id)) {
            if (!contenedorRepository.findByBarcoId(id).isEmpty()) {
                throw new BarcoConContenedoresException("No se puede eliminar el barco porque tiene contenedores asociados.");
            }
            barcoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}