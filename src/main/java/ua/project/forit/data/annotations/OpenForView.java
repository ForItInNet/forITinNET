package ua.project.forit.data.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OpenForView
{
    String value() default "none";
    String changeValueTo() default "none";
}
