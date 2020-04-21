import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

public class TinkoffTest {

    SelenideElement getCurrencyFrom = $(By.id("TCSid1"));
    SelenideElement getCurrencyTo = $(By.id("TCSid3"));
    SelenideElement courseFrom = $(By.cssSelector("div.Table__td_zJ6Up.Table__withoutPadding_xz0A8.Table__valign_top_1sby8 > div > div:nth-child(1)"));
    SelenideElement courseTo = $(By.cssSelector("div.Table__td_zJ6Up.Table__withoutPadding_xz0A8.Table__valign_top_1sby8 > div > div:nth-child(2)"));


    @Before
    public void setUp(){
        Configuration.savePageSource = false;
        open("https://www.tinkoff.ru/about/exchange/");
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
        String getCurrentPage = $(By.className("header__n-Ztx")).getText(); //данный класс является уникальным для выбранного раздела(задает желтый цвет фона у элемента)
        Assert.assertEquals("Курсы валют", getCurrentPage);
    }

    @Step("Проверка валют выставленных по умолчанию") //Пункт 6
    public void getCurrencyFromTest() {
        Assert.assertEquals("Рубль", getCurrencyFrom.getText());
        Assert.assertEquals("Евро", getCurrencyTo.getText());

        Assert.assertEquals("₽ → €", courseFrom.getText());
        Assert.assertEquals("€ → ₽", courseTo.getText());
    }


    @Step("Проверка курса 'Евро' -> 'Рубль'") //Пункт 7
    public void changeCurrencyFromTest(){
        getCurrencyFrom.click();
        $(byText("Евро")).click();
//        Assert.assertEquals("Евро", getCurrencyFrom.getText());
//        Assert.assertEquals("Рубль", getCurrencyTo.getText());
        getCurrencyFrom.shouldHave(Condition.text("Евро"));
        getCurrencyTo.shouldHave(Condition.text("Рубль"));

    }

    @Step("Проверка курса 'Евро' -> 'Доллар'") //Пункт 8-10
    public void changeCurrencyToTest() {
        getCurrencyFrom.shouldHave(Condition.text("Евро"));
        getCurrencyTo.click();

        $(byText("Доллар")).click();

        getCurrencyFrom.shouldHave(Condition.text("Евро"));
        getCurrencyTo.shouldHave(Condition.text("Доллар"));

        Assert.assertEquals("$ → €", courseFrom.getText());
        Assert.assertEquals("€ → $", courseTo.getText());
    }
}
