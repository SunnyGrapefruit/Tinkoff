import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;


public class TinkoffTest {

    @Before
    public void setUp(){
        Configuration.savePageSource = false;
        open("https://www.tinkoff.ru/about/exchange/");
    }

    public void response(WebElement url){
        List<WebElement> links = url.findElements(By.cssSelector("a"));
        for(WebElement  link : links) {
            String href = link.getAttribute("href");
            int statusCode = RestAssured.get(href).statusCode();
            Assert.assertEquals(statusCode, HttpStatus.SC_OK);
        }
    }

    @Test
    @DisplayName("Тест страницы 'Курс валют'")
    public void testCourseCurrency() {
//        headerTest();
        getCurrentPageTest();
        getCurrencyFromTest();
        changeCurrencyFromTest();
        changeCurrencyToTest();
    }

    @Step("Проверка header") //Пункт 3
    public void headerTest() {
        ElementsCollection links = $$(By.tagName("a"));
        for(WebElement  link : links) {
            String href = link.getAttribute("href");
            int statusCode = RestAssured.get(href).statusCode();
            Assert.assertEquals(statusCode, HttpStatus.SC_OK);
        }
    }

    @Step("Проверка текущего раздела") //Пункт 4
    public void getCurrentPageTest() {
        ExchangePage exchangePage = new ExchangePage();
        Assert.assertEquals("Курсы валют", exchangePage.getCurrentPage());
    }

    @Step("Проверка валют выставленных по умолчанию") //Пункт 6
    public void getCurrencyFromTest() {
        ExchangePage exchangePage = new ExchangePage();

        Assert.assertEquals("Рубль", exchangePage.getCurrencyFrom());
        Assert.assertEquals("Евро", exchangePage.getCurrencyTo());

        Assert.assertEquals("₽ → €", exchangePage.getCourseFrom());
        Assert.assertEquals("€ → ₽", exchangePage.getCourseTo());
    }

    @Step("Проверка курса 'Евро' -> 'Рубль'") //Пункт 7
    public void changeCurrencyFromTest(){
        ExchangePage exchangePage = new ExchangePage();

        exchangePage.changeCurrencyFrom("Евро");

        exchangePage.currencyFrom.shouldHave(Condition.text("Евро"));
        exchangePage.currencyTo.shouldHave(Condition.text("Рубль"));

    }

    @Step("Проверка курса 'Евро' -> 'Доллар'") //Пункт 8-10
    public void changeCurrencyToTest() {
        ExchangePage exchangePage = new ExchangePage();

        exchangePage.changeCurrencyTo("Доллар");

        exchangePage.currencyFrom.shouldHave(Condition.text("Евро"));
        exchangePage.currencyTo.shouldHave(Condition.text("Доллар"));

        Assert.assertEquals("$ → €", exchangePage.getCourseFrom());
        Assert.assertEquals("€ → $", exchangePage.getCourseTo());
    }
}
