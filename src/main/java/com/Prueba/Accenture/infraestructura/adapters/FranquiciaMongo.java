package com.Prueba.Accenture.infraestructura.adapters;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranquiciaMongo extends ReactiveMongoRepository<FranquiciaDocument, String> {
}
