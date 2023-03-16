package ir.maktab.finalprojectphase3.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ir.maktab.finalprojectphase3.data.enums.ExpertRegistrationStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpertDTO extends UserDTO {

    ExpertRegistrationStatus expertRegistrationStatus;

    Double totalScore;

    Double expertCredit;

    Set<SubServiceDTO> subServiceSet;
}
