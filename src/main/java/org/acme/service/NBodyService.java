package org.acme.service;

import javax.inject.Inject;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.repository.BodyRepository;
import org.acme.model.Body;

@ApplicationScoped
public class NBodyService {
    @Inject BodyRepository bodyRepository;

    public List<Body> computeNextStep() {
        // Logique de calcul gravitationnel
        return bodyRepository.listAll();
    }
}