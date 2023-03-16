package ir.maktab.finalprojectphase3.validation;

import ir.maktab.finalprojectphase3.exception.ValidationException;
import lombok.experimental.UtilityClass;

import java.time.YearMonth;

@UtilityClass
public class PaymentInfoValidation {
    public void validateCardNumber(String cardNumber) {
        if (cardNumber.length() != 16)
            throw new ValidationException("the card number is not valid,the length must be 16");
    }

    public void validateCvv2Number(String cvv2) {
        if (3 > cvv2.length() || cvv2.length() > 4)
            throw new ValidationException("the card number is not valid,the length must be 16");
    }

    public void validateCardExpDate(String expYear, String expMonth) {
        YearMonth cardExpDate = YearMonth.of(Integer.parseInt(expYear), Integer.parseInt(expMonth));
        if (YearMonth.now().isAfter(cardExpDate))
            throw new ValidationException("the card is expired");
    }

}
