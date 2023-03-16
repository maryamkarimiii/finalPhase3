package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    String firstName;

    String lastName;

    @Pattern(regexp = "^09[0|123]\\d{8}$", message = "the phoneNumber is not valid")
    String phoneNumber;

    @Email(regexp = "^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@"
            + "[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$", message = "the email is not valid")
    String email;

    LocalDate registrationDate;

    Double credit;

    String username;
}

