package ir.maktab.finalprojectphase3.data.dto;

import lombok.Data;

@Data
public class PaymentInfo {

    String orderTrackingNumber;

    String cardNumber;

    String cvv2;

    String expYear;

    String expMonth;

    String captcha;
}
