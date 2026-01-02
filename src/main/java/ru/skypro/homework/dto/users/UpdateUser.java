package ru.skypro.homework.dto.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(description = "UpdateUser")
@Data
public class UpdateUser {

    @Schema(description = "имя пользователя")
    @Size(min = 3, max = 10)
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @Size(min = 3,max = 10)
    private String lastName;

    //@Schema(description = "телефон пользователя",example = "+7(3968149402")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    //                 \\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}
    private String phone;
}
