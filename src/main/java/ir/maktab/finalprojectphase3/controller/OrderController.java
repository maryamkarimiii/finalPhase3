package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.OrderCreationDTO;
import ir.maktab.finalprojectphase3.data.dto.OrderDTO;
import ir.maktab.finalprojectphase3.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("add/orders")
    public void addNewOrder(@Valid @RequestBody OrderCreationDTO orderCreationDTO) {
        orderService.save(orderCreationDTO);
    }

    @GetMapping("get_orders/{trackingNumber}")
    public ResponseEntity<OrderDTO> getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        OrderDTO orderDTO = orderService.findByTrackingNumber(trackingNumber);
        return ResponseEntity.ok().body(orderDTO);
    }

    @PatchMapping("delete_orders/{trackingNumber}")
    public void deleteOrder(@PathVariable String trackingNumber) {
        orderService.softDelete(trackingNumber);
    }

    @PatchMapping("change_orders_status_to_started/{trackingNumber}")
    public void changeOrderStatusTOStartWorking(@PathVariable String trackingNumber) {
        orderService.changeStatusToStarted(trackingNumber);
    }

    @PatchMapping("change_orders_status_to_finished/{trackingNumber}")
    public void changeOrderStatusTOFinishedWorking(@PathVariable String trackingNumber) {
        orderService.changeStatusToFinished(trackingNumber);
    }

}
