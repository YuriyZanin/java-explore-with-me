package ru.practicum.ewm.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotSoonerThan2HoursValidator implements ConstraintValidator<NotSoonerThan2Hours, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext context) {
        return eventDate.isAfter(LocalDateTime.now().plusHours(2));
    }
}
