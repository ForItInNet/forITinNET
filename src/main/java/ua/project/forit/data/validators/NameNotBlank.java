package ua.project.forit.data.validators;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NameNotBlank
{
    NotBlank notBlank() default @NotBlank(message = "Одне з полів порожнє");
    Pattern pattern() default @Pattern(regexp = "(^[А-Я][а-я]*|^[A-Z][a-z]*)",
            message = "Ім'я чи фамілія повинна складатись з букв укроїнського або англійського алфавіту");
}
