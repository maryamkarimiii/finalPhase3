package ir.maktab.finalprojectphase3.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
public class Offer {

    @EmbeddedId
    private OfferId offerId;

    @Column(updatable = false)
    @CreationTimestamp
    LocalDate createDate;

    @Column(nullable = false)
    Double price;

    LocalDate workDate;

    LocalTime workTime;

    @Column(columnDefinition = "boolean default false")
    boolean confirmedByCustomer;

    @Column(columnDefinition = "boolean default false")
    boolean disable;
}
