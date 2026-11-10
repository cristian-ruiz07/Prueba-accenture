package com.Prueba.Accenture.config;

import com.Prueba.Accenture.domain.interfaces.FranquiciaRepositorio;
import com.Prueba.Accenture.domain.usecase.FranquiciaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public FranquiciaUseCase franquiciaUseCase(FranquiciaRepositorio repositorio) {
        return new FranquiciaUseCase(repositorio);
    }
}
