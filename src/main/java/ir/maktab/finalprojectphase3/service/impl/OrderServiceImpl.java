package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.CustomerDao;
import ir.maktab.finalprojectphase3.data.dao.OrderDao;
import ir.maktab.finalprojectphase3.data.dao.SubServiceDao;
import ir.maktab.finalprojectphase3.data.dto.OfferDTO;
import ir.maktab.finalprojectphase3.data.dto.OrderCreationDTO;
import ir.maktab.finalprojectphase3.data.dto.OrderDTO;
import ir.maktab.finalprojectphase3.data.enums.OrderStatus;
import ir.maktab.finalprojectphase3.data.model.Customer;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.Order;
import ir.maktab.finalprojectphase3.data.model.SubService;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.exception.ValidationException;
import ir.maktab.finalprojectphase3.mapper.OrderMapper;
import ir.maktab.finalprojectphase3.service.ExpertService;
import ir.maktab.finalprojectphase3.service.OfferService;
import ir.maktab.finalprojectphase3.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final SubServiceDao subServiceDao;
    private final CustomerDao customerDao;
    @Lazy
    @Autowired
    private OfferService offerService;

    @Lazy
    @Autowired
    private ExpertService expertService;
    private static final List<OrderStatus> STATUS_LIST =
            List.of(OrderStatus.WAITING_FOR_OFFER, OrderStatus.WAITING_FOR_CUSTOMER_CHOICE);


    @Override
    public void save(OrderCreationDTO orderDto) {
        Order order = convertOrderDtoTOModel(orderDto);
        order.setTrackingNumber(UUID.randomUUID().toString());
        validateOrderPrice(order);
        validateOrderDate(order);
        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFER);
        orderDao.save(order);
    }

    @Override
    public List<OrderDTO> findAllBySubServiceAndOrderStatus(List<SubService> subServiceList) {
        List<Order> orderList = orderDao
                .findAllBySubServiceInAndOrderStatusInAndDisableFalse(subServiceList, STATUS_LIST);
        return OrderMapper.INSTANCE.orderListsToDto(orderList);
    }

    @Override
    public OrderDTO findByTrackingNumber(String trackingNumber) {
        return OrderMapper.INSTANCE.modelToDto(findOrderByTrackingNumber(trackingNumber));
    }

    @Override
    public void changeStatusToWaitingForCustomerChose(Order order) {
        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_OFFER))
            throw new ValidationException("cant change status to waiting for offer wait for first offer");
        order.setOrderStatus(OrderStatus.WAITING_FOR_CUSTOMER_CHOICE);
        orderDao.save(order);
    }

    @Override
    public void changeStatusToStarted(String orderTrackingNumber) {
        Order order = findOrderByTrackingNumber(orderTrackingNumber);
        validateOrderStatusToStarted(order);
        order.setOrderStatus(OrderStatus.START_WORKING);
        order.setStartWorkingTime(LocalTime.now());
        orderDao.save(order);
    }

    @Override
    public void changeStatusToFinished(String orderTrackingNumber) {
        Order order = findOrderByTrackingNumber(orderTrackingNumber);
        if (!order.getOrderStatus().equals(OrderStatus.START_WORKING))
            throw new ValidationException("you cant change status to finished before expert start the work");
        order.setOrderStatus(OrderStatus.FINISH_WORKING);
        order.setFinishWorkingTime(LocalTime.now());
        orderDao.save(order);
    }

    @Override
    public void changeStatusToPayed(String orderTrackingNumber) {
        Order order = findOrderByTrackingNumber(orderTrackingNumber);
        if (!order.getOrderStatus().equals(OrderStatus.FINISH_WORKING))
            throw new ValidationException("you cant change status to payed before expert finish the work");
        order.setOrderStatus(OrderStatus.PAID);
        expertService.checkExpertOperation(order, offerService.findByOrderAndConfirmed(orderTrackingNumber).getWorkTime());
        orderDao.save(order);
    }

    @Override
    public void updateOrderAfterOfferConfirmed(Order order, Expert expert) {
        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_CUSTOMER_CHOICE))
            throw new ValidationException("you cant change status to wait for come before exist of any offered");
        order.setOrderStatus(OrderStatus.WAITING_FOR_COMING);
        order.setExpert(expert);
        orderDao.save(order);
    }

    @Override
    public List<OrderDTO> findAllByCustomer(Customer customer) {
        return OrderMapper.INSTANCE.orderListsToDto(orderDao.findAllByCustomer(customer));
    }

    @Override
    public void softDelete(String trackingNumber) {
        Order order = findOrderByTrackingNumber(trackingNumber);
        if (order.getOrderStatus().equals(OrderStatus.START_WORKING))
            throw new ValidationException("you can not cancel order because the expert start the work");
        order.setDisable(true);
        orderDao.save(order);
    }

    private Order findOrderByTrackingNumber(String trackingNumber) {
        return orderDao.findByTrackingNumberAndDisable(trackingNumber, false)
                .orElseThrow(() -> new NotFoundException
                        (String.format("the order by this %s number dose not exist", trackingNumber)));
    }

    private void validateOrderPrice(Order order) {
        Double baseAmount = order.getSubService().getBaseAmount();
        if (order.getPrice() < baseAmount)
            throw new ValidationException(String.format("order price cant be less than %f", baseAmount));
    }


    private void validateOrderDate(Order order) {
        if (order.getWorkDate().isBefore(LocalDate.now()))
            throw new ValidationException(String.format("the date cant be before %tc", new Date()));
    }

    private void validateOrderStatusToStarted(Order order) {
        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_COMING))
            throw new ValidationException("you cant change status to coming before confirmed on offer");

        OfferDTO offer = offerService.findByOrderAndConfirmed(order.getTrackingNumber());
        LocalDate offerDate = offer.getWorkDate();
        if (offerDate.isAfter(LocalDate.now()))
            throw new ValidationException
                    (String.format("you cant change order status to startWork before %tc that expert offered", offerDate));
    }

    private Order convertOrderDtoTOModel(OrderCreationDTO orderDto) {
        Order order = OrderMapper.INSTANCE.dtoToModel(orderDto);
        order.setCustomer(customerDao.findByUsername(orderDto.getCustomerUserName()).orElseThrow());
        order.setSubService(subServiceDao.findByNameAndDisable(orderDto.getSubServiceName(), false).orElseThrow());
        return order;
    }
}
