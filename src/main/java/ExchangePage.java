import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ExchangePage {

    //TinkoffTest
    SelenideElement currentPage = $(By.className("header__n-Ztx"));
    SelenideElement header1 = $(By.className("header__1lZpj"));
    SelenideElement header2 = $(By.className("header__3Wh47"));
    SelenideElement footer = $(By.className("footer__8Y9k5"));
    SelenideElement currencyFrom = $(By.id("TCSid1"));
    SelenideElement currencyTo = $(By.id("TCSid3"));
    SelenideElement courseFrom = $$(".DesktopExchange__th_AXtbR").get(1);
    SelenideElement courseTo = $$(".DesktopExchange__th_AXtbR").get(2);

    //CbrCourse
    SelenideElement valueCourseFrom= $$(".Table__valign_bottom_1XlMR").get(1);

    public String getCurrentPage(){
        String current = currentPage.getText();
        return current;
    }

    public String getCurrencyFrom(){
        String currency = currencyFrom.getText();
        return currency;
    }

    public String getCurrencyTo(){
        String currency = currencyTo.getText();
        return currency;
    }

    public String getCourseFrom(){
        String currency = courseFrom.getText();
        return currency;
    }

    public String getCourseTo(){
        String currency = courseTo.getText();
        return currency;
    }

    public void changeCurrencyFrom(String course){
        currencyFrom.click();
        $(byText(course)).click();
        currencyFrom.shouldHave(Condition.text(course));
    }

    public void changeCurrencyTo(String course){
        currencyTo.click();
        $(byText(course)).click();
        currencyTo.shouldHave(Condition.text(course));
    }

    public String getValueCourseFrom(){
        String currency = valueCourseFrom.getText();
        return currency;
    }
}
