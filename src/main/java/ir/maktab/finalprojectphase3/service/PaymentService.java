package ir.maktab.finalprojectphase3.service;

import ir.maktab.finalprojectphase3.data.dto.PaymentInfo;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    void creditPayment(String orderTrackingNumber);

    void onlinePayment(String orderTrackingNumber);

    void validateCardInfo(PaymentInfo paymentInfo);

    void validateCaptcha(HttpServletRequest request);
}
