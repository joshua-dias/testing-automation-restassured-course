package br.ce.joshuadias;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CursoRest {
    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;
    @BeforeClass
    public static void Setup(){
        baseURI = "https://restapi.wcaquino.me";
//      basePath = "/v2";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        reqSpec = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectStatusCode(200);
        resSpec = resBuilder.build();

        requestSpecification = reqSpec;
        responseSpecification = resSpec;
    }
    public static void main() {

    }

    @Test
    public void devoFazerPesquisasAvancadasComXPath() {

                given()
                .when()
                    .get("/usersXML")
                .then()
                    .body(hasXPath("count(/users/user)", is("3")))
                    .body(hasXPath("/users/user[@id = '1']"))
                    .body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
                    .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Luizinho"), containsString("Zezinho"))))
                    .body(hasXPath("/users/user/name", is("João da Silva")))
                    .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
                    .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                    .body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
                    .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
                    .body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
        ;
    }
}