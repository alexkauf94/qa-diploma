package utils;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;

import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DataGenerator {
    private static LocalDate futureDate = LocalDate.now().plusYears(1);
    private static Faker faker = new Faker();
    private static String validCardHolderName =  faker.name().fullName();
    private static String validCVV = valueOf(faker.number().numberBetween(111, 991));

    @Value
    public static class CardData {
        String cardNumber;
        String month;
        String year;
        String ownerName;
        String cvc;
    }

    public static CardData getApprovedCard() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
               validCardHolderName, validCVV);
    }

    public static CardData getDeclinedCard() {
        LocalDate futureDate = LocalDate.now().plusYears(1);
        return new CardData("4444 4444 4444 4442", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidCardDataIfEmptyAllFields() {
        return new CardData("", "", "", "", "");
    }

    public static CardData getInvalidCardNumberIfEmpty() {
        return new CardData("", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidCardNumberIfLess16Sym() {
        return new CardData("4444 4444 4444 44", futureDate.format(ofPattern("MM")),
                valueOf(futureDate.getYear()).substring(2), validCardHolderName, validCVV);
    }

    public static CardData getInvalidCardNumberIfOutOfDatabase() {
        return new CardData("5578334444444441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidMonthIfEmpty() {
        return new CardData("4444 4444 4444 4441", "", valueOf(futureDate.getYear()).substring(2), validCardHolderName,
                validCVV);
    }

    public static CardData getInvalidNumberOfMonthIfMore12() {
        return new CardData("4444 4444 4444 4441", "35", valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidNumberOfMonthIfOneDigit() {
        return new CardData("4444 4444 4444 4441", "3", valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, validCVV);
    }


    public static CardData getInvalidNumberOfMonthIfZero() {
        return new CardData("4444 4444 4444 4441", "00", valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidYearIfZero() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), "00",
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidNumberOfYearIfOneDigit() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), "7",
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidYearIfEmpty() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), "",
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidYearIfBeforeCurrentYear() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(LocalDate.now().minusYears(1)).substring(2),
                validCardHolderName, validCVV);
    }

    public static CardData getInvalidCardOwnerNameIfEmpty() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                "", validCVV);
    }

    public static CardData getInvalidCardOwnerNameIfNumericAndSpecialCharacters() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                "67893!№", validCVV);
    }

    public static CardData getInvalidCardOwnerNameIfRussianLetters() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                "Иван Иванов", validCVV);
    }

    public static CardData getInvalidCvcIfEmpty() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, "");
    }

    public static CardData getInvalidCvcIfOneDigit() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")),
                valueOf(futureDate.getYear()).substring(2), validCardHolderName, "5");
    }

    public static CardData getInvalidCvcIfTwoDigits() {
        return new CardData("4444 4444 4444 4441",futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, "25");
    }

    public static CardData getInvalidCvvIfThreeZero() {
        return new CardData("4444 4444 4444 4441", futureDate.format(ofPattern("MM")), valueOf(futureDate.getYear()).substring(2),
                validCardHolderName, "000");
    }

}
