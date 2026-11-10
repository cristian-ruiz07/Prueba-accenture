package com.Prueba.Accenture.config;

import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.usecase.ActualizarNombreFranquicia;
import com.Prueba.Accenture.domain.usecase.CrearFranquicia;
import com.Prueba.Accenture.domain.usecase.ObtenerFranquicia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CrearFranquicia crearFranquicia(FranquiciaRepositorio repositorio) {
        return new CrearFranquicia(repositorio);
    }

    @Bean
    public ObtenerFranquicia obtenerFranquicia(FranquiciaRepositorio repositorio) {
        return new ObtenerFranquicia(repositorio);
    }

    @Bean
    public ActualizarNombreFranquicia actualizarNombreFranquicia(FranquiciaRepositorio repositorio) {
        return new ActualizarNombreFranquicia(repositorio);
    }
}
