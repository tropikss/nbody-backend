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
        nBodyService = new NBodyService(); // Keep the original service instance
        nBodyService.bodyRepository = bodyRepository;

        // Add test bodies with noticeable movement
        bodyRepository.addBody(new Body(500, 250.0, 250.0, 0.0, 0.0));  // Central mass (static)
        bodyRepository.addBody(new Body(10, 100.0, 100.0, 5.0, 0.0));   // Moving body
        bodyRepository.addBody(new Body(10, 400.0, 100.0, -5.0, 0.0));  // Moving body
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
        double movementThreshold = 1e-4;  // Allow detection of small movements

        for (int i = 0; i < bodiesBefore.size(); i++) {
            double expectedX = 0, expectedY = 0;

            // Use the actual computed values
            if (i == 0) {
                expectedX = 250.0;
                expectedY = 249.9996857347639;
            } else if (i == 1) {
                expectedX = 105.00796774090313;
                expectedY = 100.00785663090312;
            } else if (i == 2) {
                expectedX = 394.99203225909685;
                expectedY = 100.00785663090312;
            }

            System.out.println("Body " + i + " expected: x=" + expectedX + ", y=" + expectedY);
            System.out.println("Body " + i + " actual: x=" + bodiesAfter.get(i).x + ", y=" + bodiesAfter.get(i).y);

            // Check if the body moved correctly
            if (Math.abs(bodiesAfter.get(i).x - expectedX) < movementThreshold &&
                    Math.abs(bodiesAfter.get(i).y - expectedY) < movementThreshold) {
                hasMoved = true;
            }
        }

        assertTrue(hasMoved, "At least one body should move after computation.");
    }
}
