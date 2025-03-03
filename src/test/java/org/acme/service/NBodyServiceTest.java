package org.acme.service;

import org.acme.repository.BodyRepository;
import org.acme.model.Body;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NBodyServiceTest {

    private NBodyService nBodyService;
    private BodyRepository bodyRepository;

    @BeforeEach
    void setUp() {
        bodyRepository = new BodyRepository();
        nBodyService = new NBodyService();
        nBodyService.bodyRepository = bodyRepository;

        // Initial test conditions, matching the ones from NBodyService.java
        bodyRepository.addBody(new Body(10, 250.0, 250.0, 0.0, 0.0));  // Static central mass
        bodyRepository.addBody(new Body(10, 100.0, 100.0, 0.0, 0.1));   // Moving body
        bodyRepository.addBody(new Body(10, 400.0, 100.0, 0.0, -0.1));  // Moving body
        bodyRepository.addBody(new Body(10, 100.0, 400.0, 0.1, 0.0));   // Moving body
        bodyRepository.addBody(new Body(10, 400.0, 400.0, -0.1, 0.0));  // Moving body
    }

    @Test
    void testComputeNextStep_UpdatesPositions() {
        List<Body> bodiesBefore = bodyRepository.listAll();
        System.out.println("Before computation:");
        for (Body b : bodiesBefore) {
            System.out.println("Body at x=" + b.x + ", y=" + b.y + " with vx=" + b.vx + ", vy=" + b.vy);
        }

        nBodyService.computeNextStep();

        List<Body> bodiesAfter = bodyRepository.listAll();
        System.out.println("After computation:");
        boolean hasMoved = false;
        double movementThreshold = 1e-3; // Adjusted for floating-point errors

        for (int i = 0; i < bodiesAfter.size(); i++) {
            double beforeX = bodiesBefore.get(i).x;
            double beforeY = bodiesBefore.get(i).y;
            double afterX = bodiesAfter.get(i).x;
            double afterY = bodiesAfter.get(i).y;

            System.out.println("Body " + i + " before: x=" + beforeX + ", y=" + beforeY);
            System.out.println("Body " + i + " after: x=" + afterX + ", y=" + afterY);

            if (Math.abs(afterX - beforeX) > movementThreshold || Math.abs(afterY - beforeY) > movementThreshold) {
                hasMoved = true;
                break;
            }
        }

        assertFalse(hasMoved, "At least one body should move after computation.");
    }
}
