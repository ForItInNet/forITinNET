package ua.project.forit.data.validators;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public @interface TextNotBlank
{
    NotBlank notBlank() default @NotBlank(message = "Одне з полів пусте");
    Pattern pattern() default @Pattern(regexp = "^[A-zА-яіІїЇєЄ\"'`|#№$!%*().:,0-9]{5,150}",
            message = "Одне з полів містить заборонені символи");
}