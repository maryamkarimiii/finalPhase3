package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.maktab.finalprojectphase3.data.enums.OrderStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    String trackingNumber;

    String customerUserName;

    String subServiceName;

    String expertUsername;

    LocalDate orderDate;

    String description;

    Double price;

    LocalDate workDate;

    String address;

    OrderStatus orderStatus;

    @JsonProperty("canceled")
    boolean disable;
}
