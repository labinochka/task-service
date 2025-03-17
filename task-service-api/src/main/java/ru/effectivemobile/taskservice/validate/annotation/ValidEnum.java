package ru.effectivemobile.taskservice.validate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.effectivemobile.taskservice.validate.validator.EnumValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "Invalid enum value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}

