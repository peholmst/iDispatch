package net.pkhapps.idispatch.domain.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation annotation to be placed on constructors and fields that are present only because they are needed
 * by the persistence framework. They do not have any business value.
 */
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface UsedByPersistenceFramework {
}
