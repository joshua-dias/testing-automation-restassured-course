package br.ce.joshuadias;

import org.junit.Test;

import static io.restassured.RestAssured.given;

public class VerbosTesteCursoRest {
    @Test
    public void DeveSalvarUsuario() {
                given()
                    .log().all()
                    .contentType("application/json")
                    .body("{\"name\": \"Joshua\", \"age\": 50 }")
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
        ;
    }
}
