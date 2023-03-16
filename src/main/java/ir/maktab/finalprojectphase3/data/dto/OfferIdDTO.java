package ir.maktab.finalprojectphase3.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class OfferIdDTO {
    @NotNull
    @NotBlank
    String orderTrackingNumber;

    @NotNull
    @NotBlank
    String expertUserName;
}
