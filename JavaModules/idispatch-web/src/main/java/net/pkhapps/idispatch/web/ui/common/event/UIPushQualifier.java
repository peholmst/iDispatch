package net.pkhapps.idispatch.web.ui.common.event;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * TODO Document me!
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier
public @interface UIPushQualifier {
}
