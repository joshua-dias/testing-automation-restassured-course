package br.ce.joshuadias;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class VerbosPostCursoRest {
    @Test
    public void deveSalvarUsuario() {
        given()
                    .log().all()
                    .contentType("application/json")
                    .body("{\"name\": \"Joshua\", \"age\": 50 }")
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
                    .body("id", is(notNullValue()))
                    .body("name", is("Joshua"))
                    .body("age", is(50))
        ;
    }
    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given()
                    .log().all()
                    .contentType("application/json")
                    .body("{\"age\": 50 }")
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(400)
                    .body("id", is(nullValue()))
                    .body("error", is("Name é um atributo obrigatório"))
        ;
    }
    @Test
    public void deveSalvarUsuarioViaXML() {
        given()
                    .log().all()
                    .contentType(ContentType.XML)
                    .body("<user><name>Jose</name><age>50</age></user>")
                .when()
                    .post("https://restapi.wcaquino.me/usersXML")
                .then()
                    .log().all()
                    .statusCode(201)
                    .body("user.@id", is(notNullValue()))
                    .body("user.name", is("Jose"))
                    .body("user.age", is("50"))
        ;
    }
    @Test
    public void deveAlterarUsuario() {
        given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body("{\"name\": \"UsuarioAlterado\", \"age\": 80 }")
                .when()
                    .put("https://restapi.wcaquino.me/users/1")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("name", is("UsuarioAlterado"))
                    .body("age", is(80))
                    .body("salary", is(1234.5678f))
        ;
    }
    @Test
    public void devoCustomizarURL() {
        given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body("{\"name\": \"UsuarioAlterado\", \"age\": 80 }")
                    .pathParam("entidade", "users")
                    .pathParam("userId", "1")
                .when()
                    .put("https://restapi.wcaquino.me/{entidade}/{userId}")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("id", is(1))
                    .body("name", is("UsuarioAlterado"))
                    .body("age", is(80))
                    .body("salary", is(1234.5678f))
            ;
    }
    @Test
    public void devoDeletarUsuario() {
        given()
                    .log().all()
                .when()
                    .delete("https://restapi.wcaquino.me/users/1")
                .then()
                    .log().all()
                    .statusCode(204)
            ;
    }
    @Test
    public void naoDeveDeletarUsuarioInexistente() {
        given()
                    .log().all()
                .when()
                    .delete("https://restapi.wcaquino.me/users/1000")
                .then()
                    .log().all()
                    .statusCode(400)
                    .body("error", is("Registro inexistente"))
        ;
    }
    @Test
    public void deveSalvarUsuarioUsandoMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Usuario via map");
        params.put("age", 25);

        given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
                    .body("id", is(notNullValue()))
                    .body("name", is("Usuario via map"))
                    .body("age", is(25))
        ;
    }
    @Test
    public void deveSalvarUsuarioUsandoObjeto() {
        User user = new User("Usuario via objeto", 35);

        given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
                    .body("id", is(notNullValue()))
                    .body("name", is("Usuario via objeto"))
                    .body("age", is(35))
        ;
    }
    @Test
    public void deveDeserializarObjetoAoSalvarUsuario() {
        User user = new User("Deserializado", 35);

        User usuarioInserido = given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post("https://restapi.wcaquino.me/users")
                .then()
                    .log().all()
                    .statusCode(201)
                    .extract().body().as(User.class)
        ;
        System.out.println(usuarioInserido);
        Assert.assertEquals("Deserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(35));
        Assert.assertThat(usuarioInserido.getId(), notNullValue());
    }

}
