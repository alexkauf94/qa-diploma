package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private final SelenideElement mainElement = $$(".heading").find(exactText("Путешествие дня"));
    private final SelenideElement payWithCardButton = $$("button").find(exactText("Купить"));
    private final SelenideElement payWithCreditButton = $$("button").find(exactText("Купить в кредит"));
    private final SelenideElement formOfPayment = $("#root > div > h3");

    public MainPage() {
        open(System.getProperty("base.url"));
        shouldBeVisible();
    }

    @Step("Главная страница должна отображаться")
    public void shouldBeVisible() {
        mainElement.shouldBe(visible);
    }

    @Step("Выбрать оплату по карте")
    public void payWithCard() {
        payWithCardButton.click();
        formOfPayment.shouldHave(text("Оплата по карте"));
    }

    @Step("Выбрать оплату в кредит")
    public void payWithCredit() {
        payWithCreditButton.click();
        formOfPayment.shouldHave(text("Кредит по данным карты"));
    }
}
