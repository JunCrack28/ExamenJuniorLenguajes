package com.examen.examen.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 17)
    private String vin;
    
    @Column(nullable = false)
    private LocalDate fecha;
    
    @ManyToOne
    @JoinColumn(name = "id_barco", nullable = false)
    private Barco barco;
}