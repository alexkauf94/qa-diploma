package tests;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.PaymentPage;
import pages.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.DataGenerator.*;
import static utils.DbConnectionHelper.*;

public class DebitPurchaseTest extends BaseTest {
    private MainPage mainPage;
    private PaymentPage paymentPage;

    @BeforeEach
    void setUpPayWithCard() {
        mainPage = new MainPage();
        paymentPage = new PaymentPage();
        mainPage.payWithCard();
    }

    @Test
    @Description("Проверка успешной оплаты тура при заполнении корректными данными карты")
    public void shouldSuccessPayWithValidCard() {
        var cardData = getApprovedCard();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldSuccessNotification();

        var expectedStatus = "APPROVED";
        var paymentInfo = getCardRequestStatus();
        var orderInfo = getOrderInfo();

        assertEquals(expectedStatus, paymentInfo.getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
    }

    @Test
    @Description("Покупка тура в кредит с помощью отклоняемой карты")
    public void shouldFailurePayWithDeclinedCard() {
        var cardData = getDeclinedCard();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldFailureNotification();

        var expectedStatus = "DECLINED";
        var paymentInfo = getCardRequestStatus();
        var orderInfo = getOrderInfo();

        assertEquals(expectedStatus, paymentInfo.getStatus());
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
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
    public void shouldFailurePaymentIfNameRussianLetters() {
        var cardData = getInvalidCardOwnerNameIfRussianLetters();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': пустое поле")
    public void shouldFailurePaymentIfCVCIsEmpty() {
        var cardData = getInvalidCvcIfEmpty();
        paymentPage.fillCardData(cardData);
        ElementsCollection fieldSub = $$(".input__sub");
        final SelenideElement cvvFieldSub = fieldSub.get(2);
        cvvFieldSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': Поле содержит одну цифру")
    public void shouldFailurePaymentIfCVCHasOneDigit() {
        var cardData = getInvalidCvcIfOneDigit();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': Поле содержит две цифры")
    public void shouldFailurePaymentIfCVCHasTwoDigits() {
        var cardData = getInvalidCvcIfTwoDigits();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'CVC/CVV': Поле содержит три нуля")
    public void shouldFailurePaymentIfCVCHasThreeZeros() {
        var cardData = getInvalidCvvIfThreeZero();
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
    @Description("Валидация поля 'Месяц': пустое поле")
    public void shouldFailurePaymentIfEmptyNumberOfMonth() {
        var cardData = getInvalidMonthIfEmpty();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldEmptyFieldNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': Поле содержит двузначное число более 12")
    public void shouldFailurePaymentIfNumberOfMonthIsMoreThan12() {
        var cardData = getInvalidNumberOfMonthIfMore12();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldInvalidExpiredDateNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': Поле содержит одно число")
    public void shouldFailurePaymentIfNumberOfMonthHasOneDigit() {
        var cardData = getInvalidNumberOfMonthIfOneDigit();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Валидация поля 'Месяц': Поле содержит два нуля")
    public void shouldFailurePaymentIfNumberOfMonthIsTwoZeros() {
        var cardData = getInvalidNumberOfMonthIfZero();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldInvalidExpiredDateNotification();
    }

    @Test
    @Description("Валидация поля 'Год': Поле содержит два нуля")
    public void shouldFailurePaymentIfYearHasTwoZeros () {
        var cardData = getInvalidYearIfZero();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldExpiredDatePassNotification();
    }

    @Test
    @Description("Валидация поля 'Год': Поле содержит одно число")
    public void shouldFailurePaymentIfYearHasOneDigit () {
        var cardData = getInvalidNumberOfYearIfOneDigit();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldImproperFormatNotification();
    }

    @Test
    @Description("Оплата по карте с истекшим сроком действия (введен год ранее текущего)")
    public void shouldFailurePaymentIfYearBeforeCurrentYear () {
        var cardData = getInvalidYearIfBeforeCurrentYear();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldExpiredDatePassNotification();
    }

    @Test
    @Description("Валидация поля 'Год': Пустое поле")
    public void shouldFailurePaymentIfEmptyYear () {
        var cardData = getInvalidYearIfEmpty();
        paymentPage.fillCardData(cardData);
        paymentPage.shouldEmptyFieldNotification();
    }

}
