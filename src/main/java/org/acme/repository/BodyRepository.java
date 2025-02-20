package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.model.Body;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BodyRepository implements PanacheRepository<Body> {
    // Quarkus-Panache simplifie les requêtes : pas besoin d'implémentation SQL
}