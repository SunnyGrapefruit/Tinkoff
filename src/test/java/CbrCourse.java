import com.codeborne.selenide.Condition;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import org.openqa.selenium.By;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;


public class CbrCourse {

    private static ValidatableResponse requestSpecification;

    @BeforeClass
    public static void setupResponse() {
        requestSpecification = RestAssured.given().contentType(ContentType.JSON)
                .get("https://www.cbr-xml-daily.ru/daily_json.js")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тест страницы 'Курс валют'")
    public void testCbrApi() {
        getWith200StatusTest();
        getHeaderTest();
        getCourseTest();
        getDateTest();
        saveRates();
    }

    @Step("Проверка статуса") //Пункт 12
    public void getWith200StatusTest() {
        requestSpecification.assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Step("Проверка заголовка Content-Type") //Пункт 13
    public void getHeaderTest() {
        requestSpecification.assertThat()
                .contentType(ContentType.JSON);
    }

    @Step("Проверка наличия \"USD\" и \"EUR\"") //Пункт 14
    public void getCourseTest() {
        requestSpecification.assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("rates-schema.json"));
    }


    @Step("Проверка даты") //Пункт 15
    public void getDateTest() {
        Assert.assertEquals(
                LocalDate.now(),
                LocalDate.parse(
                        requestSpecification.extract()
                                .jsonPath()
                                .getString("Timestamp"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                )
        );
        Assert.assertEquals(
                LocalDate.now().plusDays(1),
                LocalDate.parse(
                        requestSpecification.extract()
                                .jsonPath()
                                .getString("Date"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                )
        );

    }


    @Step("Проверка курсов") //Проверка наличия евро и доллара
    public void saveRates() {

       ExchangePage exchangePage = new ExchangePage();
       String re =  requestSpecification.extract().body().asString();
       ExchangeData exchangeData = new Gson().fromJson(re, ExchangeData.class);

       double usd = exchangeData.getValute().getUsd().getValue();
       double eur = exchangeData.getValute().getEur().getValue();

       double co =  eur/usd;

       open("https://www.tinkoff.ru/about/exchange/");

       Assert.assertEquals(eur, exchangePage.getValueCourseFrom());
       exchangePage.changeCurrencyFrom("Доллар");
       Assert.assertEquals(exchangePage.getValueCourseFrom(), String.format("%.2f", co));
    }
}