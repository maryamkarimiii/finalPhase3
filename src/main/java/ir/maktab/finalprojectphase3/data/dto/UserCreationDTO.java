package ir.maktab.finalprojectphase3.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationDTO {

    @NotNull
    @NotBlank
    String firstName;

    @NotNull
    @NotBlank
    String lastName;

    @NotNull
    @Pattern(regexp = "^09[0|123]\\d{8}$", message = "the phoneNumber is not valid")
    String phoneNumber;

    @NotNull
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@"
            + "[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$", message = "the email is not valid")
    String email;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=\\S+$).{8}$", message = "the password is not valid")
    String password;
}
