package com.Prueba.Accenture.config;

import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.usecase.FranquiciaUseCase;
import com.Prueba.Accenture.domain.usecase.ProductUseCase;
import com.Prueba.Accenture.domain.usecase.SucursalUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public FranquiciaUseCase franquiciaUseCase(FranquiciaRepositorio repositorio) {
        return new FranquiciaUseCase(repositorio);
    }

    @Bean
    public SucursalUseCase sucursalUseCase(FranquiciaRepositorio repositorio) {
        return new SucursalUseCase(repositorio);
    }

    @Bean
    public ProductUseCase productoUseCase(FranquiciaRepositorio repositorio) {
        return new ProductUseCase(repositorio);
    }
}
