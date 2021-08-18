package br.ce.joshuadias;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static io.restassured.path.xml.XmlPath.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FileTeste {
    @Test
    public void deveObrigarEnvioArquivo(){
                given()
                        .log().all()
                .when()
                        .post("http://restapi.wcaquino.me/upload")
                .then()
                        .log().all()
                        .statusCode(404) //Deveria ser 400
                        .body("error", is("Arquivo não enviado"))
                ;
    }
    @Test
    public void deveEnviarArquivo() {
                    given()
                            .log().all()
                            .multiPart("arquivo", new File("src/main/resources/users.pdf"))
                    .when()
                            .post("http://restapi.wcaquino.me/upload")
                    .then()
                            .log().all()
                            .statusCode(200)
                            .body("name", is("users.pdf"))
                    ;
    }
    @Test
    public void naoDeveEnviarArquivoGrande() {
                given()
                        .log().all()
                        .multiPart("arquivo", new File("src/main/resources/ChromeSetup.exe"))
                .when()
                        .post("http://restapi.wcaquino.me/upload")
                .then()
                        .log().all()
                        .statusCode(413)
                        .time(lessThan(5000L))
        ;
    }
    @Test
    public void deveBaixarArquivo() throws IOException {
        byte[] image = given()
                        .log().all()
                .when()
                        .get("http://restapi.wcaquino.me/download")
                .then()
//                        .log().all()
                        .statusCode(200)
                        .extract().asByteArray();

        File imagem = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        System.out.println(imagem.length());
        Assert.assertThat(imagem.length(), lessThan(100000L));
    }
    @Test
    public void deveFazerAutenticacaoComTokenJWT(){
            Map<String, String> login = new HashMap<String, String>();
            login.put("email", "joshuadias09@gmail.com");
            login.put("senha", "123456");

            //Login na API
            //Receber o token
             String token = given()
                     .log().all()
                     .body(login)
                     .contentType(ContentType.JSON)
            .when()
                     .post("https://barrigarest.wcaquino.me/signin")
            .then()
                     .log().all()
                     .statusCode(200)
                     .extract().path("token");


             //Obter as contas
            given()
                    .log().all()
                    .header("Authorization", "JWT " + token)

            .when()
                    .get("https://barrigarest.wcaquino.me/contas")
            .then()
                    .log().all()
                    .statusCode(200)
                    .body("nome", hasItem("Conta de teste"))
            ;
    }
    @Test
    public void deveAcessarAplicacaoWeb(){
        //login
        String cookie = given()
                .log().all()
                .formParam("email", "joshuadias09@gmail.com")
                .formParam("senha", "123456")
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .when()
                .post("https://seubarriga.wcaquino.me/logar")
        .then()
                .log().all()
                .statusCode(200)
                .extract().header("set-cookie")
        ;

        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        //obter conta
                String body = given()
                        .log().all()
                        .cookie("connect.sid", cookie)
                .when()
                        .get("https://seubarriga.wcaquino.me/contas")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
                        .extract().body().asString();
                ;
                System.out.println("===========================");
                XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
                System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));

    }
}
