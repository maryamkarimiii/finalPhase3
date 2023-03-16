package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.OrderDao;
import ir.maktab.finalprojectphase3.data.dao.WalletDao;
import ir.maktab.finalprojectphase3.data.dto.OfferDTO;
import ir.maktab.finalprojectphase3.data.dto.PaymentInfo;
import ir.maktab.finalprojectphase3.data.enums.OrderStatus;
import ir.maktab.finalprojectphase3.data.model.Customer;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.Order;
import ir.maktab.finalprojectphase3.data.model.Wallet;
import ir.maktab.finalprojectphase3.exception.ValidationException;
import ir.maktab.finalprojectphase3.service.OfferService;
import ir.maktab.finalprojectphase3.service.OrderService;
import ir.maktab.finalprojectphase3.service.PaymentService;
import ir.maktab.finalprojectphase3.validation.PaymentInfoValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final double EXPERT_COMMISSION = 0.7;
    private final OfferService offerService;
    private final OrderService orderService;
    private final OrderDao orderDao;
    private final WalletDao walletDao;

    @Override
    @Transactional
    public void creditPayment(String orderTrackingNumber) {
        Order order = findOrderByTrackingNumber(orderTrackingNumber);
        validateOrderStatusBeforePayment(order);
        Customer customer = order.getCustomer();
        Expert expert = order.getExpert();
        OfferDTO offerDTO = offerService.findByOrderAndConfirmed(orderTrackingNumber);
        Double offerPrice = offerDTO.getPrice();
        validateCustomerCreditBeforePayment(customer.getWallet().getCredit(), offerPrice);
        transferMoney(customer.getWallet(), expert.getWallet(), offerPrice);
        orderService.changeStatusToPayed(order.getTrackingNumber());
    }

    @Override
    @Transactional
    public void onlinePayment(String orderTrackingNumber) {
        Order order = findOrderByTrackingNumber(orderTrackingNumber);
        validateOrderStatusBeforePayment(order);
        Wallet expertWallet = order.getExpert().getWallet();
        OfferDTO offerDTO = offerService.findByOrderAndConfirmed(orderTrackingNumber);
        Double offerPrice = offerDTO.getPrice();
        expertWallet.setCredit(expertWallet.getCredit() + offerPrice * EXPERT_COMMISSION);
        walletDao.save(expertWallet);
        orderService.changeStatusToPayed(order.getTrackingNumber());
    }

    @Override
    public void validateCardInfo(PaymentInfo paymentInfo) {
        PaymentInfoValidation.validateCardNumber(paymentInfo.getCardNumber());
        PaymentInfoValidation.validateCvv2Number(paymentInfo.getCvv2());
        PaymentInfoValidation.validateCardExpDate(paymentInfo.getExpYear(), paymentInfo.getExpMonth());
    }

    @Override
    public void validateCaptcha(HttpServletRequest request) {
        String userInput = request.getParameter("captcha");
        String storedCaptcha = (String) request.getSession().getAttribute("captcha");
        if (!userInput.equals(storedCaptcha)) {
            request.getSession().invalidate();
            throw new ValidationException("the captcha dose not match");
        }
    }

    private void transferMoney(Wallet source, Wallet destination, double money) {
        source.setCredit(source.getCredit() - money);
        walletDao.save(source);
        destination.setCredit(destination.getCredit() + money * EXPERT_COMMISSION);
        walletDao.save(destination);
    }

    private void validateCustomerCreditBeforePayment(double customerCredit, double offerPrice) {
        if (customerCredit < offerPrice)
            throw new ValidationException("your credit is less than" + offerPrice);
    }

    private void validateOrderStatusBeforePayment(Order order) {
        if (!order.getOrderStatus().equals(OrderStatus.FINISH_WORKING))
            throw new ValidationException("the work dose not finish yet.change the status to finished work before payment");
    }

    private Order findOrderByTrackingNumber(String trackingNumber) {
        return orderDao.findByTrackingNumberAndDisable(trackingNumber, false).orElseThrow();
    }


}
