import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class TinkoffTest {

    @Test
    public void getCurrentPageTest() {
        open("https://www.tinkoff.ru/about/exchange/");
        $(By.className("header__3Gok1")).shouldHave(text("Курсы валют"));
    }



    @Test
    public void changeCurrencyFromTest() {
        open("https://www.tinkoff.ru/about/exchange/");
        $(By.id("TCSid1")).shouldHave(text("Евро"));
        $(By.id("TCSid3")).shouldHave(text("Рубль"));
    }

    @Test
    public void changeCurrencyToTest() {
        open("https://www.tinkoff.ru/about/exchange/");
        $(By.id("TCSid1")).shouldHave(text("Евро"));
        $(By.id("TCSid3")).shouldHave(text("Доллар"));
    }

}
