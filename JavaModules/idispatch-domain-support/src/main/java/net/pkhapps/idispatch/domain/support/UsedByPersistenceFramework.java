package net.pkhapps.idispatch.domain.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface UsedByPersistenceFramework {
}
