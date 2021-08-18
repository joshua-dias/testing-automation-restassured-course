package br.ce.joshuadias;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class SchemaTeste {
    @Test
    public void deveValidarSchemaJson(){
                given()
                        .log().all()
                .when()
                        .get("https://restapi.wcaquino.me/users")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
                ;
    }
}
