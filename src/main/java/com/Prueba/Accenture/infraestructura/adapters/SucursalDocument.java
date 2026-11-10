package com.Prueba.Accenture.infraestructura.adapters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDocument {

    private String id;
    private String nombre;
    private String descripcion;
    private List<ProductoDocument> productos = new ArrayList<>();
}
