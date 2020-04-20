import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;

public class TinkoffTest {

    SelenideElement getCurrencyFrom = $(By.id("TCSid1"));
    SelenideElement getCurrencyTo = $(By.id("TCSid3"));
//    String getCourseFrom= $(By.xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div/div[3]/div/div/div[3]/div/div[1]/div[2]/div/div[1]")).getText();
//    String getCourseTo= $(By.xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div/div[3]/div/div/div[3]/div/div[1]/div[2]/div/div[2]")).getText();

    @Before
    public void setUp(){
        Configuration.savePageSource = false;
        open("https://www.tinkoff.ru/about/exchange/");
    }

    @Test
    @DisplayName("Тест страницы 'Курс валют'")
    public void testCourseCurrency() {
//        headerTest();
//        footerTest();
        getCurrentPageTest();
        getCurrencyFromTest();
//        changeCurrencyFromTest();
//        changeCurrencyToTest();
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
        String getCurrentPage = $(By.className("header__n-Ztx")).getText(); //данный класс является уникальным для выбранного раздела(задает желтый цвет фона у выбранного элемента
        Assert.assertEquals("Курсы валют", getCurrentPage);
    }

    @Step("Проверка валют выставленных по умолчанию") //Пункт 6
    public void getCurrencyFromTest() {

        Assert.assertEquals("Рубль", getCurrencyFrom.getText());
        Assert.assertEquals("Евро", getCurrencyTo.getText());

        String  getCourseFrom= $(By.xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div/div[3]/div/div/div[3]/div/div[1]/div[2]/div/div[1]")).getText();
        String  getCourseTo= $(By.xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div/div[3]/div/div/div[3]/div/div[1]/div[2]/div/div[2]")).getText();
        Assert.assertEquals("₽ → €", getCourseFrom);
        Assert.assertEquals("€ → ₽", getCourseTo);
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

        String  getCourseFrom= $(By.xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div/div[3]/div/div/div[3]/div/div[1]/div[2]/div/div[1]")).getText();
        String  getCourseTo= $(By.xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div/div[3]/div/div/div[3]/div/div[1]/div[2]/div/div[2]")).getText();
        Assert.assertEquals("$ → €", getCourseFrom);
        Assert.assertEquals("€ → $", getCourseTo);

    }

}
