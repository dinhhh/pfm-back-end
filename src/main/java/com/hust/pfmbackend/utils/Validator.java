package com.hust.pfmbackend.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

public class Validator {

    public static boolean isValidObject(Object o) {
        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        return violations.size() == 0;
    }

}
