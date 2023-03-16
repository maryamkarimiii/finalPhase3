package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubServiceDTO {
    @NotNull
    @NotBlank
    String name;

    @NotNull
    Double baseAmount;

    @NotNull
    @NotBlank
    String description;

    @NotNull
    String serviceName;
}
