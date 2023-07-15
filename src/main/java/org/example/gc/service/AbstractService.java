package org.example.gc.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.example.gc.dto.EntityDto;

import java.util.Set;

public abstract class AbstractService {
    protected static final String MESSAGE_FOUND =
            "%s was successfully found.";
    protected static final String MESSAGE_INSERTED =
            "%s was successfully inserted.";
    protected static final String MESSAGE_DELETED =
            "%s was successfully deleted.";
    protected static final String MESSAGE_UPDATED =
            "%s was successfully updated.";

    protected void validate(EntityDto dto) {
        Set<ConstraintViolation<EntityDto>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}