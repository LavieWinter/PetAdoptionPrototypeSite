package com.example.PetAdoption.dominio.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoAdminRoleValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoAdminRole {
    String message() default "ADMIN role is not allowed in signup";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
