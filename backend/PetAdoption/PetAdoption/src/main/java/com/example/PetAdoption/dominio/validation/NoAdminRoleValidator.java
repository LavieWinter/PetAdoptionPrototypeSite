package com.example.PetAdoption.dominio.validation;

import com.example.PetAdoption.dominio.enums.UserRoles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class NoAdminRoleValidator implements ConstraintValidator<NoAdminRole, Set<UserRoles>> {
    @Override
    public boolean isValid(Set<UserRoles> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.contains(UserRoles.ADMIN);
    }
}
