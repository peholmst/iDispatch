package net.pkhapps.idispatch.shared.domain.base;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Stereotype annotation for Spring beans that act as domain services, as opposed to application services.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DomainService {
}
