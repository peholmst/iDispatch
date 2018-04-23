package net.pkhapps.idispatch.domain;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationFailedException extends Exception {

    private final Set<ConstraintViolation<?>> violations;

    public ValidationFailedException(Set<? extends ConstraintViolation<?>> violations) {
        this.violations = Collections.unmodifiableSet(new HashSet<>(violations));
    }

    public static void throwIfNotEmpty(Set<? extends ConstraintViolation<?>> violations) throws ValidationFailedException {
        if (violations.size() > 0) {
            throw new ValidationFailedException(violations);
        }
    }

    public Set<ConstraintViolation<?>> getViolations() {
        return violations;
    }

    public Set<String> getViolationMessages(Locale locale) {
        // TODO Internationalize
        return getViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
    }

}
