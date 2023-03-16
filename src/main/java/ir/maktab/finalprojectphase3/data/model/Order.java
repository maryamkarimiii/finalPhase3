package ir.maktab.finalprojectphase3.data.model;

import ir.maktab.finalprojectphase3.data.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String trackingNumber;

    @ManyToOne
    Customer customer;

    @ManyToOne
    SubService subService;

    @ManyToOne
    Expert expert;

    @CreationTimestamp
    LocalDate orderDate;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    LocalDate workDate;

    LocalTime startWorkingTime;

    LocalTime finishWorkingTime;

    @Column(nullable = false)
    String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus orderStatus;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
