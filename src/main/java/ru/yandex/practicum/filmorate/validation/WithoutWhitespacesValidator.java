package ru.yandex.practicum.filmorate.validation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WithoutWhitespacesValidator implements ConstraintValidator<WithoutWhitespacesValidation, String>
{
    public boolean isValid(String login, ConstraintValidatorContext cxt) {
        return (login != null) && !login.contains(" ");
    }
}