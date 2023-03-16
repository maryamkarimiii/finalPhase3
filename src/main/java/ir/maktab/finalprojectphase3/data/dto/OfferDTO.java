package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OfferDTO {

    @NotNull
    @NotBlank
    String orderTrackingNumber;

    @NotNull
    @NotBlank
    String expertUserName;

    @NotNull
    Double price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    LocalDate workDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalTime workTime;

}
