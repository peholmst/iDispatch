package net.pkhsolutions.idispatch.data;

import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.pkhsolutions.idispatch.entity.AbstractEntity;

/**
 *
 * @author Petter Holmström
 */
public class BuilderPropertyDescriptor<E extends AbstractEntity, B extends AbstractEntity.AbstractEntityBuilder> implements java.io.Serializable {

    private String name;
    private Method entityReadMethod;
    private Method builderWriteMethod;
    // TODO Serialize methods

    private BuilderPropertyDescriptor(String name, Method entityReadMethod, Method builderWriteMethod) {
        this.name = name;
        this.entityReadMethod = entityReadMethod;
        this.builderWriteMethod = builderWriteMethod;
    }

    public String getName() {
        return name;
    }

    public boolean isWritable() {
        return builderWriteMethod != null;
    }

    public Class<?> getType() {
        return entityReadMethod.getReturnType();
    }

    public Object getValue(E entity) {
        if (entity == null) {
            return null;
        } else {
            try {
                return entityReadMethod.invoke(entity);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Could not get value", ex);
            } catch (InvocationTargetException ex) {
                throw ExceptionUtils.unwrap(ex);
            }
        }
    }

    public E setValue(B builder, Object value) {
        if (builderWriteMethod == null) {
            throw new IllegalStateException("No write method is present");
        }
        try {
            builderWriteMethod.invoke(builder, value);
            return (E) builder.build();
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Could not set value", ex);
        } catch (InvocationTargetException ex) {
            throw ExceptionUtils.unwrap(ex);
        }
    }

    public static <E extends AbstractEntity, B extends AbstractEntity.AbstractEntityBuilder> Collection<BuilderPropertyDescriptor<E, B>> sanForProperties(Class<E> entityClass, Class<B> builderClass) {
        Set<BuilderPropertyDescriptor<E, B>> descriptors = new HashSet<>();
        for (Method potentialReadMethod : entityClass.getMethods()) {
            if (potentialReadMethod.getReturnType() == Void.TYPE && potentialReadMethod.getParameterTypes().length == 0) {
                String methodName = potentialReadMethod.getName();
                if (methodName.length() > 3 && methodName.startsWith("get")) {
                    // This is a read method, now let's try to find a corresponding write method
                    String baseName = methodName.substring(3);
                    String writeMethodName = "with" + baseName;
                    Method writeMethod;
                    try {
                        writeMethod = builderClass.getMethod(writeMethodName, potentialReadMethod.getReturnType());
                    } catch (NoSuchMethodException | SecurityException ex) {
                        writeMethod = null;
                    }
                    descriptors.add(new BuilderPropertyDescriptor<E, B>(Introspector.decapitalize(baseName), potentialReadMethod, writeMethod));
                }
            }
        }
        return descriptors;
    }
}
