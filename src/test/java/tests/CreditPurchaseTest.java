package tests;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import jdk.jfr.Description;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.PaymentPage;
import tests.BaseTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.DataGenerator.*;
import static utils.DbConnectionHelper.*;

public class CreditPurchaseTest extends BaseTest {

    MainPage mainPage;
    PaymentPage paymentPage;

    @BeforeEach
    void setUpPayWithCredit() {
        mainPage = new MainPage();
        paymentPage = new PaymentPage();
        mainPage.payWithCredit();
    }

    @Test
    @Description("Проверка успешной оплаты тура в кредит при заполнении корректными данными карты")
    public void shouldSuccessCreditRequestWithValidCard() {
        var cardData = getApprovedCard();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldSuccessNotification();

        var expectedStatus = "APPROVED";
        var actualStatus = getCardStatusForCreditRequest();
        assertEquals(expectedStatus, actualStatus);

        var bankIdExpected = getBankId();
        var paymentIdActual = getPaymentId();
        assertNotNull(bankIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(bankIdExpected, paymentIdActual);
    }

    @Test
    @Description("Покупка тура с помощью отклоняемой карты")
    public void shouldFailurePayIfWithDeclinedCard() {
        var cardData = getDeclinedCard();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldFailureNotification();

        var expectedStatus = "DECLINED";
        var actualStatus = getCardStatusForCreditRequest();
        assertEquals(expectedStatus, actualStatus);

        var bankIdExpected = getBankId();
        var paymentIdActual = getPaymentId();
        assertNotNull(bankIdExpected);
        assertNotNull(paymentIdActual);
        assertEquals(bankIdExpected, paymentIdActual);
    }


    @Test
    @Description("Валидация поля 'Номер карты': пустое поле")
    public void shouldFailurePaymentIfEmptyCardNumber() {
        var cardData = getInvalidCardNumberIfEmpty();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldEmptyFieldNotification();
    }

    @Test
    @Description("Валидация поля 'Номер карты': Поле содержит количество цифр менее 16")
    public void shouldFailurePaymentIfCardNumberIfLess16Sym() {
        var cardData = getInvalidCardNumberIfLess16Sym();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Номер карты не содержится в базе данных банка")
    public void shouldFailurePaymentIfCardNumberIfOutOfBase() {
        var cardData = getInvalidCardNumberIfOutOfDatabase();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldFailureNotification();
    }

    @Test
    @Description("Валидация поля 'Владелец': пустое поле.")
    public void shouldFailurePaymentIfEmptyCardholderName() {
        var cardData = getInvalidCardOwnerNameIfEmpty();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldEmptyFieldNotification();
    }

    @Test
    @Description("Валидация поля 'Владелец': Поле содержит цифры и спец символы")
    public void shouldFailurePaymentIfNameNumeric() {
        var cardData = getInvalidCardOwnerNameIfNumericAndSpecialCharacters();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'Владелец': русские буквы")
    public void shouldFailurePaymentIfNameHasRussianLetters() {
        var cardData = getInvalidCardOwnerNameIfRussianLetters();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Отправка пустой формы покупки тура")
    public void shouldFailurePaymentIfTourFieldsAreEmpty() {
        var cardData = getInvalidCardDataIfEmptyAllFields();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
        paymentPage.shouldImproperFormatNotification();
        paymentPage.shouldImproperFormatNotification();
        paymentPage.shouldEmptyFieldNotification();
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': пустое поле")
    public void shouldFailurePaymentIfEmptyCvc() {
        var cardData = getInvalidCvcIfEmpty();
        paymentPage.fillCardData(cardData);
        final ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cvvFieldSub = fieldSub.get(2);
        cvvFieldSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': Поле содержит одну цифру")
    public void shouldFailurePaymentIfCvcHasOneDigit() {
        var cardData = getInvalidCvcIfOneDigit();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': Поле содержит две цифры")
    public void shouldFailurePaymentIfCvcHasTwoDigits() {
        var cardData = getInvalidCvcIfTwoDigits();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': Поле содержит три нуля")
    public void shouldFailurePaymentIfCvvHasThreeZeros() {
        var cardData = getInvalidCvvIfThreeZero();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldSuccessNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': пустое поле")
    public void shouldFailurePaymentIfEmptyNumberOfMonth() {
        var cardData = getInvalidMonthIfEmpty();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldEmptyFieldNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': Поле содержит двузначное число более 12")
    public void shouldFailurePaymentIfNumberOfMonthIsMore12() {
        var cardData = getInvalidNumberOfMonthIfMore12();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldInvalidExpiredDateNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': Поле содержит одно число")
    public void shouldFailurePaymentIfNumberOfMonthIsOneDigit() {
        var cardData = getInvalidNumberOfMonthIfOneDigit();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': Поле содержит два нуля")
    public void shouldFailurePaymentIfNumberOfMonthHasTwoZeros() {
        var cardData = getInvalidNumberOfMonthIfZero();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldInvalidExpiredDateNotification();
    }

    @Test
    @Description("Валидация поля 'Год': Поле содержит два нуля")
    public void shouldFailurePaymentIfYearHasTwoZeros() {
        var cardData = getInvalidYearIfZero();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldExpiredDatePassNotification();
    }


    @Test
    @Description("Валидация поля 'Год': Поле содержит одно число")
    public void shouldFailurePaymentIfYearOneDigit() {
        var cardData = getInvalidNumberOfYearIfOneDigit();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Оплата по карте с истекшим сроком действия (введен год ранее текущего)")
    public void shouldFailurePaymentIfYearBeforeCurrentYear() {
        var cardData = getInvalidYearIfBeforeCurrentYear();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldExpiredDatePassNotification();
    }


    @Test
    @Description("Валидация поля 'Год': Пустое поле")
    public void shouldFailurePaymentIfEmptyYear() {
        var cardData = getInvalidYearIfEmpty();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldEmptyFieldNotification();
    }

}
