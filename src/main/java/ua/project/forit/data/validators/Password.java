package ua.project.forit.data.validators;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password
{
    NotBlank notBlank() default @NotBlank(message = "Пароль не може бути пустим");
    Pattern pattern() default @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$",
            message = "Пароль повинен містити від 8 до 16 символів. У нього повинні входити, принаймні, одна велика літера, одна мала літера і одна цифра");
}
