package org.acme.repository;

import java.util.List;
import java.util.ArrayList;
import org.acme.model.Body;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BodyRepository {
    private List<Body> bodies = new ArrayList<>();

    private double speed = 0.1;

    public double getSpeed() {
        return speed;
    }
    
    public void setSpeed(double speed) {
        System.out.println("Speed set to " + speed);
        this.speed = speed;
    }

    public List<Body> listAll() {
        return bodies;
    }

    public void addBody(Body body) {
        bodies.add(body);
    }

    public void clear() {
        bodies.clear();
    }

    public int getLength() {
        return bodies.size();
    }

    public void setAll(List<Body> newBodies) {
        bodies = newBodies;
    }
}