package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.ApplicationException;
import javax.validation.ConstraintViolation;

/**
 *
 * @author peholmst
 */
@ApplicationException(rollback = true)
public class ValidationFailedException extends Exception {

    private final Set<? extends ConstraintViolation<?>> violations;

    public <E> ValidationFailedException(Set<ConstraintViolation<E>> violations) {
        this.violations = Collections.unmodifiableSet(new HashSet<>(violations));
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }

    public static <E> void throwIfNonEmpty(Set<ConstraintViolation<E>> violations) throws ValidationFailedException {
        if (violations.size() > 0) {
            throw new ValidationFailedException(violations);
        }
    }
}
