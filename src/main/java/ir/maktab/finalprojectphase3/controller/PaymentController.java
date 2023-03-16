package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.PaymentInfo;
import ir.maktab.finalprojectphase3.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/online_payment")
    public String cardPayment(@ModelAttribute PaymentInfo paymentInfo, HttpServletRequest httpServletRequest) {
        paymentService.validateCardInfo(paymentInfo);
        paymentService.validateCaptcha(httpServletRequest);
        paymentService.onlinePayment(paymentInfo.getOrderTrackingNumber());
        return "OK";
    }

    @PutMapping("/credit_payment/{orderTrackingNumber}")
    public void creditPayment(@PathVariable String orderTrackingNumber) {
        paymentService.creditPayment(orderTrackingNumber);
    }

}
