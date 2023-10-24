package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import utils.DataGenerator;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.time.Duration.ofSeconds;

public class PaymentPage {
    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement numberOfMonthField = $("input[placeholder='08']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final ElementsCollection fieldSet = $$(".input__control");
    private final SelenideElement cardholderField = fieldSet.get(3);
    private final SelenideElement cvvField = $("input[placeholder='999']");

    private final SelenideElement improperFormat = $(byText("Неверный формат"));
    private final SelenideElement emptyField = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement invalidExpiredDate = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement expiredDatePass = $(byText("Истёк срок действия карты"));
    private final SelenideElement successNote = $(byText("Операция одобрена Банком."));
    private final SelenideElement failureNote = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement continueButton = $$("button").find(exactText("Продолжить"));

    public void fillCardData(DataGenerator.CardData cardData) {
        cardNumberField.setValue(cardData.getCardNumber());
        numberOfMonthField.setValue(cardData.getMonth());
        yearField.setValue(cardData.getYear());
        cardholderField.setValue(cardData.getOwnerName());
        cvvField.setValue(cardData.getCvc());
        continueButton.click();
    }

    public void shouldImproperFormatNotification() {
        improperFormat.shouldBe(visible);
    }

    public void shouldEmptyFieldNotification() {
        emptyField.shouldBe(visible);
    }

    public void shouldInvalidExpiredDateNotification() {
        invalidExpiredDate.shouldBe(visible);
    }

    public void shouldExpiredDatePassNotification() {
        expiredDatePass.shouldBe(visible);
    }

    public void shouldSuccessNotification() {
        successNote.shouldBe(visible, ofSeconds(10));
    }

    public void shouldFailureNotification() {
        failureNote.shouldBe(visible, ofSeconds(10));
    }
}
