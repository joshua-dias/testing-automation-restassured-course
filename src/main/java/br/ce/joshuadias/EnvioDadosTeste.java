package br.ce.joshuadias;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EnvioDadosTeste {
    @Test
    public void deveEnviarValorViaQuery(){
                given()
                        .log().all()
                        .queryParam("format", "xml")
                .when()
                        .get("https://restapi.wcaquino.me/v2/users")
                .then()
                        .log().all()
                        .statusCode(200)
                        .contentType(ContentType.XML)
                        .contentType(containsString("utf-8"))
                ;
    }
    @Test
    public void deveEnviarValorViaHeader(){
        given()
                .log().all()
                .accept(ContentType.JSON)
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }
}

