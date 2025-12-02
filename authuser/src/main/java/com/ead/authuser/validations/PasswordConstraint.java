package com.ead.authuser.validations;

import com.ead.authuser.validations.impl.PasswordConstraintImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintImpl.class)
//Definição de onde a validação pode ser utilizada, metodo e campo.
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default """
            Invalid password! Password must contain at least one digit[0-9], at least one lowecase Latin character[a-z],
            at least one uppercase Ltin character[A-Z], at least one special character like !@#&()-:;',?/*~$^+=<>,
            a length of at least 6 characters and a maximum of 20 characters""";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
