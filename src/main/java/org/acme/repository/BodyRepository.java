package org.acme.repository;

import java.util.List;
import java.util.ArrayList;
import org.acme.model.Body;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BodyRepository {
    private List<Body> bodies = new ArrayList<>();

    public List<Body> listAll() {
        return bodies;
    }

    public void addBody(Body body) {
        bodies.add(body);
    }

    public void clear() {
        bodies.clear();
    }

    public void setAll(List<Body> newBodies) {
        bodies = newBodies;
    }
}