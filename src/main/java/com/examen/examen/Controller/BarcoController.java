package com.examen.examen.Controller;

import com.examen.examen.Exception.BarcoConContenedoresException;
import com.examen.examen.Exception.CapacidadExcedidaException;
import com.examen.examen.Model.Barco;
import com.examen.examen.Service.BarcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barcos")
public class BarcoController {
    
    @Autowired
    private BarcoService barcoService;
    
    @GetMapping
    public List<Barco> getAllBarcos() {
        return barcoService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Barco> getBarcoById(@PathVariable Long id) {
        return barcoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Barco createBarco(@RequestBody Barco barco) {
        return barcoService.save(barco);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Barco> updateBarco(@PathVariable Long id, @RequestBody Barco barcoDetails) {
        try {
            return barcoService.update(id, barcoDetails)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (CapacidadExcedidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarco(@PathVariable Long id) {
        try {
            if (barcoService.delete(id)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (BarcoConContenedoresException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @ExceptionHandler({BarcoConContenedoresException.class, CapacidadExcedidaException.class})
    public ResponseEntity<String> handleExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}