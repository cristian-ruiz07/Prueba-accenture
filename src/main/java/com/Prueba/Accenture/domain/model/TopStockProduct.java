package com.Prueba.Accenture.domain.model;

public record TopStockProduct(String sucursalId, String sucursalNombre, String productoId, String productoNombre, int stock) {
    public static TopStockProduct de(Sucursal sucursal, Producto producto) {
        return new TopStockProduct(
                sucursal.getId(), sucursal.getNombre(),
                producto.getId(), producto.getNombre(), producto.getStock());
    }
}
