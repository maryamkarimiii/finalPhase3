package ir.maktab.finalprojectphase3.data.dto;

import ir.maktab.finalprojectphase3.controller.CustomerController;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDTO {

    @NotNull
    private String oldPassword;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=\\S+$).{8}$", message = "the password is not valid")
    private String newPassword;

    @NotNull
    private String confirmPassword;
}
