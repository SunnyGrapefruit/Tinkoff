import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;


public class CbrCourse {

    private static ValidatableResponse requestSpecification;
    public static WebDriver driver;

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
                LocalDate.now().minusDays(1),
                LocalDate.parse(
                        requestSpecification.extract()
                                .jsonPath()
                                .getString("Timestamp"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                )
        );
        Assert.assertEquals(
                LocalDate.now(),
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
        double usdRate = requestSpecification.extract()
                .jsonPath()
                .getDouble("Valute.USD.Value");
        double eurRate = requestSpecification.extract()
                .jsonPath()
                .getDouble("Valute.EUR.Value");

        open("https://www.tinkoff.ru/about/exchange/");
        String getCourseFrom= $(By.cssSelector("div.Table__td_zJ6Up.Table__withoutPadding_xz0A8.Table__valign_top_1sby8 > div:nth-child(1) > div:nth-child(2) > div.Text__text_primary_28uo7")).getText();

        Assert.assertEquals(eurRate, getCourseFrom);

        $(By.id("TCSid1")).click();
        $(byText("Доллар")).click();

//        Assert.assertEquals(eurRate, "ну тут какой-то курс вычислить");
    }
}