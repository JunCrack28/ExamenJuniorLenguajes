package com.examen.examen.Controller;

import com.examen.examen.Exception.CapacidadExcedidaException;
import com.examen.examen.Model.Contenedor;
import com.examen.examen.Service.ContenedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorController {
    
    @Autowired
    private ContenedorService contenedorService;
    
    @GetMapping
    public List<Contenedor> getAllContenedores(@RequestParam(required = false) Long barcoId) {
        if (barcoId != null) {
            return contenedorService.findByBarcoId(barcoId);
        }
        return contenedorService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Contenedor> getContenedorById(@PathVariable Long id) {
        return contenedorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Contenedor> createContenedor(@RequestBody Contenedor contenedor) {
        try {
            return ResponseEntity.ok(contenedorService.save(contenedor));
        } catch (CapacidadExcedidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Contenedor> updateContenedor(@PathVariable Long id, @RequestBody Contenedor contenedorDetails) {
        return contenedorService.update(id, contenedorDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenedor(@PathVariable Long id) {
        if (contenedorService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CapacidadExcedidaException.class)
    public ResponseEntity<String> handleCapacidadExcedidaException(CapacidadExcedidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}