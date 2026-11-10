package com.Prueba.Accenture.infraestructura.adapters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDocument {

    private String id;
    private String nombre;
    private String descripcion;
    private int stock;
}
