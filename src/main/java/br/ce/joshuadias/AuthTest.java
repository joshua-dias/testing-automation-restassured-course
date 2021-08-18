package br.ce.joshuadias;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AuthTest {
    @Test
    public void deveAcessarSWAPI(){
                given()
                        .log().all()
                .when()
                        .get("https://swapi.dev/api/people/1")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Luke Skywalker"))
        ;
    }
    @Test
    public void deveObterClima(){
                given()
                        .log().all()
                        .queryParam("q", "Fortaleza,BR")
                        .queryParam("appid", "c62912c58c06eed7903cf65b1b951620")
                        .queryParam("units", "metric")
                .when()
                        .get("https://api.openweathermap.org/data/2.5/weather")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Fortaleza"))
                        .body("coord.lon", is(-38.5247f))
                        .body("timezone", is(-10800))
                ;
    }
    @Test
    public void naoDeveAcessarSemSenha(){
                given()
                    .log().all()
                .when()
                    .get("https://restapi.wcaquino.me/basicauth")
                .then()
                    .log().all()
                    .statusCode(401)
        ;
    }
    @Test
    public void deveFazerAutenticacaoBasica(){
                given()
                    .log().all()
                .when()
                    .get("https://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                    .log().all()
                    .statusCode(200)
                        .body("status", is("logado"))
        ;
    }
    @Test
    public void deveFazerAutenticacaoBasicaComParametros(){
                given()
                    .log().all()
                    .auth().basic("admin", "senha")
                .when()
                    .get("https://restapi.wcaquino.me/basicauth")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("status", is("logado"))
        ;
    }
    @Test
    public void deveFazerAutenticacaoBasicaComChallenge(){
                 given()
                    .log().all()
                    .auth().preemptive().basic("admin", "senha")
                .when()
                    .get("https://restapi.wcaquino.me/basicauth2")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("status", is("logado"))
        ;
    }
}
