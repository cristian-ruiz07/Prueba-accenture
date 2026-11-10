package com.Prueba.Accenture.infraestructura.adapters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Subdocumento: se guarda dentro de FranquiciaDocument, no tiene coleccion propia
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDocument {

    private String id;
    private String nombre;
    private List<ProductoDocument> productos = new ArrayList<>();
}
