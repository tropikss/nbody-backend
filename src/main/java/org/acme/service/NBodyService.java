package org.acme.service;

import jakarta.inject.Inject;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.repository.BodyRepository;
import org.acme.model.Body;

@ApplicationScoped
public class NBodyService {
    @Inject BodyRepository bodyRepository;

    @PostConstruct
    void init() {
        if (bodyRepository.getLength() == 0) { // Vérifie si la base est vide
            bodyRepository.addBody(new Body(10, 250, 250, 0.0, 0.0));

            // Ajoute quelques planètes avec des positions et vitesses initiales différentes
            bodyRepository.addBody(new Body(10, 100, 100, 0.0, 0.1));
            bodyRepository.addBody(new Body(10, 400, 100, 0.0, -0.1));
            bodyRepository.addBody(new Body(10, 100, 400, 0.1, 0.0));
            bodyRepository.addBody(new Body(10, 400, 400, -0.1, 0.0));
        }
    }

    public List<Body> computeNextStep() {
        double dt = bodyRepository.getSpeed();
        double G = 2.0;               // Constante gravitationnelle "toy" pour simulation visuelle
        double epsilon = 1e-1;        // Pour éviter la division par zéro
        double maxSpeed = 10.0;       // Vitesse maximale autorisée
        List<Body> bodies = bodyRepository.listAll(); // Récupération des corps depuis le repository

        // Calcul des accélérations et mises à jour des vitesses
        for (Body b : bodies) {
            double ax = 0;
            double ay = 0;
            for (Body other : bodies) {
                if (b != other) {
                    double dx = other.x - b.x;
                    double dy = other.y - b.y;
                    double distance = Math.sqrt(dx * dx + dy * dy) + epsilon;
                    double force = G * b.mass * other.mass / (distance * distance);
                    // L'accélération est dans la direction du vecteur (dx, dy)
                    ax += force * dx / (distance * b.mass);
                    ay += force * dy / (distance * b.mass);
                }
            }
            // Mise à jour de la vitesse
            b.vx += ax * dt;
            b.vy += ay * dt;

            // Vérifie si la vitesse dépasse la vitesse maximale autorisée
            double speed = Math.sqrt(b.vx * b.vx + b.vy * b.vy);
            if (speed > maxSpeed) {
                // Si la vitesse est trop élevée, on tue le corps (le retire de la liste)
                bodies.remove(b);
                continue;
            }
        }

        // Mise à jour des positions en fonction des vitesses
        for (Body b : bodies) {
            b.x += b.vx * dt;
            b.y += b.vy * dt;

            // Gestion des rebonds pour rester dans le canvas 500x500
            if (b.x < 0) {
                b.x = 0;
                b.vx = -b.vx;
            }
            if (b.x > 500) {
                b.x = 500;
                b.vx = -b.vx;
            }
            if (b.y < 0) {
                b.y = 0;
                b.vy = -b.vy;
            }
            if (b.y > 500) {
                b.y = 500;
                b.vy = -b.vy;
            }
        }

        bodyRepository.setAll(bodies);
        return bodies;
    }
}