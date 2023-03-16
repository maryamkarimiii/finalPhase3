package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDTO {
    @NotNull
    @NotBlank
    String fullName;

    @NotNull
    @NotBlank
    String username;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=\\S+$).{8}$", message = "the password is not valid")
    String password;
}
