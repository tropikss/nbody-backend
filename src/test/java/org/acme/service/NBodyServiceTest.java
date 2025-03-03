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
    }

    @Test
    void testComputeNextStep_UpdatesPositions() {
        List<Body> bodiesBefore = bodyRepository.listAll();
        nBodyService.computeNextStep();
        List<Body> bodiesAfter = bodyRepository.listAll();

        assertEquals(bodiesBefore.size(), bodiesAfter.size(), "Le nombre de corps doit rester identique");
        assertNotEquals(bodiesBefore.get(0).x, bodiesAfter.get(0).x, "Les positions doivent changer après un pas de simulation");
    }
}
