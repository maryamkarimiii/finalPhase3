package ir.maktab.finalprojectphase3.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class ServiceDTO {
    @NotBlank
    @NotNull
    private String name;
}
