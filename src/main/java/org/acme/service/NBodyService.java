package org.acme.service;

import jakarta.inject.Inject;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.repository.BodyRepository;
import org.acme.model.Body;

@ApplicationScoped
public class NBodyService {
    @Inject BodyRepository bodyRepository;

    public List<Body> computeNextStep() {
        double dt = 0.1;              // Pas de temps
        double G = 6.67430e-11;         // Constante gravitationnelle (peut être ajustée pour la simulation)
        double epsilon = 1e-3;          // Petite valeur pour éviter la division par zéro
        List<Body> bodies = bodyRepository.listAll(); // Ici, tes corps sont stockés en mémoire
    
        // Calcul des nouvelles vitesses en fonction des forces gravitationnelles
        for (Body b : bodies) {
            double ax = 0;
            double ay = 0;
            for (Body other : bodies) {
                if (b != other) {
                    double dx = other.x - b.x;
                    double dy = other.y - b.y;
                    double distance = Math.sqrt(dx * dx + dy * dy) + epsilon;
                    // Calcul de la force : F = G * m1 * m2 / distance²
                    double force = G * b.mass * other.mass / (distance * distance);
                    // Accélération = force / masse, direction vers other
                    ax += force * dx / (distance * b.mass);
                    ay += force * dy / (distance * b.mass);
                }
            }
            // Mise à jour de la vitesse
            b.vx += ax * dt;
            b.vy += ay * dt;
        }
        
        // Mise à jour des positions en fonction des vitesses
        for (Body b : bodies) {
            b.x += b.vx * dt;
            b.y += b.vy * dt;
        }
        bodyRepository.setAll(bodies);
        return bodies;
    }
    
}