package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
public class NBodyResourceTest {

    @Test
    void testComputeBodiesAPI() {
        RestAssured.given()
            .when().get("/nbody/compute")
            .then()
            .statusCode(200)
            .body("$", not(empty()));
    }
}
