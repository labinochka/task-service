package ru.effectivemobile.taskservice.validate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.effectivemobile.taskservice.validate.annotation.ValidEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        acceptedValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        return acceptedValues.contains(value.name());
    }
}

