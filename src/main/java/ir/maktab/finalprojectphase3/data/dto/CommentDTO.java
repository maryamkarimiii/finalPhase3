package ir.maktab.finalprojectphase3.data.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentDTO {

    @NotNull
    @NotBlank
    String expertUserName;

    @NotNull
    @NotBlank
    String customerUserName;

    @NotNull
    @Min(1)
    @Max(5)
    Integer score;

    String customerComment;
}
