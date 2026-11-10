package com.Prueba.Accenture.infraestructura.adapters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franquicias")
public class FranquiciaDocument {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private List<SucursalDocument> sucursales = new ArrayList<>();
}
