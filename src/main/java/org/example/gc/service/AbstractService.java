package org.example.gc.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.example.gc.dto.EntityDto;
import org.example.gc.dto.TagDto;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.Parameters;
import org.example.gc.parameters.TagParameters;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractService {
    private static final String ERROR_PARAMS_VIOLATION =
            "Parameters have the following violations : [%s]";
    protected static final String MESSAGE_FOUND =
            "%s was successfully found.";
    protected static final String MESSAGE_INSERTED =
            "%s was successfully inserted.";
    protected static final String MESSAGE_DELETED =
            "%s was successfully deleted.";
    protected static final String MESSAGE_UPDATED =
            "%s was successfully updated.";

    protected void validate(EntityDto<?> dto) {
        Set<ConstraintViolation<EntityDto<?>>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(dto);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}