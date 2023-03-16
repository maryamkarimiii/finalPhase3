package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import ir.maktab.finalprojectphase3.data.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CriteriaSearchDTO {
    static final Double MIN_SCORE = 1.0;
    static final Double MAX_SCORE = 5.0;

    Role role;

    String firstName;

    String lastName;

    @Email(regexp = "^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@"
            + "[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$", message = "the email is not valid")
    String email;

    String speciality;

    @Min(0)
    @JsonSetter(nulls = Nulls.SKIP)
    Double minScore = MIN_SCORE;

    @Max(5)
    @JsonSetter(nulls = Nulls.SKIP)
    Double maxScore = MAX_SCORE;
}
