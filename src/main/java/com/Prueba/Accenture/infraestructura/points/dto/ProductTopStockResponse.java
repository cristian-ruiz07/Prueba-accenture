package com.Prueba.Accenture.infraestructura.points.dto;

import com.Prueba.Accenture.domain.model.TopStockProduct;

public record ProductTopStockResponse(String sucursalId, String sucursalNombre, String productoId, String productoNombre, int stock) {

    public static ProductTopStockResponse de(TopStockProduct p){
        return  new ProductTopStockResponse(
                p.sucursalId(),p.sucursalNombre(),p.productoId(),p.productoNombre(),p.stock()
        );
    }
}
