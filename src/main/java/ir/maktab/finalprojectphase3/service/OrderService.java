package ir.maktab.finalprojectphase3.service;


import ir.maktab.finalprojectphase3.data.dto.OrderCreationDTO;
import ir.maktab.finalprojectphase3.data.dto.OrderDTO;
import ir.maktab.finalprojectphase3.data.model.Customer;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.Order;
import ir.maktab.finalprojectphase3.data.model.SubService;

import java.util.List;

public interface OrderService extends BaseService<OrderCreationDTO> {

    List<OrderDTO> findAllBySubServiceAndOrderStatus(List<SubService> subServiceList);

    OrderDTO findByTrackingNumber(String trackingNumber);

    void changeStatusToWaitingForCustomerChose(Order order);

    void changeStatusToStarted(String orderTrackingNumber);

    void changeStatusToFinished(String orderTrackingNumber);

    void changeStatusToPayed(String orderTrackingNumber);

    void updateOrderAfterOfferConfirmed(Order order, Expert expert);

    List<OrderDTO> findAllByCustomer(Customer customer);

    void softDelete(String trackingNumber);
}
