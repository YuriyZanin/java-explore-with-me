package ru.practicum.ewm.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotSoonerThan2HoursValidator.class)
public @interface NotSoonerThan2Hours {
    String message() default "не может быть раньше чем через два часа";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}