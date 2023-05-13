package ru.yandex.practicum.filmorate.validation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValidation, LocalDate>
{
    public boolean isValid(LocalDate date, ConstraintValidatorContext cxt) {
        LocalDate filmBirthday = LocalDate.of(1895, 12, 28);
        return (date != null) && (date.isAfter(filmBirthday) || date.isEqual(filmBirthday));
    }
}