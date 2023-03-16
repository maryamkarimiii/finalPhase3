package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class OrderCreationDTO {
    @NotNull
    @NotBlank
    String customerUserName;

    @NotNull
    @NotBlank
    String subServiceName;

    @NotNull
    @NotBlank
    String description;

    @NotNull
    Double price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    LocalDate workDate;

    @NotNull
    @NotBlank
    String address;
}
