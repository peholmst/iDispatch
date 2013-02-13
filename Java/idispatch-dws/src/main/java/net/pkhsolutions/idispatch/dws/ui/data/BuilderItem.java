package net.pkhsolutions.idispatch.dws.ui.data;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.pkhsolutions.idispatch.entity.AbstractEntity;

/**
 *
 * @author Petter Holmström
 */
public abstract class BuilderItem<E extends AbstractEntity, B extends AbstractEntity.AbstractEntityBuilder> implements Item {

    private Class<E> entityClass;
    private Class<B> builderClass;
    // TODO Serialize classes
    private Map<Object, BuilderProperty> propertyMap = new HashMap<>();
    private E entity;

    protected BuilderItem(Class<E> entityClass, Class<B> builderClass) {
        this.entityClass = entityClass;
        this.builderClass = builderClass;
        scanForProperties();
    }

    private void scanForProperties() {
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
                    propertyMap.put(Introspector.decapitalize(baseName), new BuilderProperty(potentialReadMethod, writeMethod));
                }
            }
        }
    }

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        setInternalEntity(entity);
        for (BuilderProperty property : propertyMap.values()) {
            property.fireValueChangeEvent();
            property.fireReadOnlyStatusChangeEvent();
        }
    }

    protected void setInternalEntity(E entity) {
        this.entity = entity;
    }

    @Override
    public Property getItemProperty(Object id) {
        return propertyMap.get(id);
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        return Collections.unmodifiableSet(propertyMap.keySet());
    }

    @Override
    public final boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Item properties cannot be explicitly added");
    }

    @Override
    public final boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Item properties cannot be explicitly removed");
    }

    protected B createNewBuilder() {
        try {
            if (getEntity() == null) {
                return builderClass.getConstructor().newInstance();
            } else {
                return builderClass.getConstructor(entityClass).newInstance(getEntity());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException("Could not create new builder instance", ex);
        } catch (InvocationTargetException ex) {
            throw unwrap(ex);
        }
    }

    public class BuilderProperty implements Property, Property.ValueChangeNotifier, Property.ReadOnlyStatusChangeNotifier {

        private Method entityReadMethod;
        private Method builderWriteMethod;
        // TODO Serialize methods
        private boolean readOnly;
        private Set<Property.ValueChangeListener> valueChangeListeners = new HashSet<>();
        private Set<ReadOnlyStatusChangeListener> readOnlyStatusChangeListeners = new HashSet<>();

        protected BuilderProperty(Method entityReadMethod, Method builderWriteMethod) {
            this.entityReadMethod = entityReadMethod;
            this.builderWriteMethod = builderWriteMethod;
        }

        @Override
        public Object getValue() {
            if (getEntity() == null) {
                return null;
            } else {
                try {
                    return entityReadMethod.invoke(getEntity());
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Could not get value", ex);
                } catch (InvocationTargetException ex) {
                    throw unwrap(ex);
                }
            }
        }

        @Override
        public void setValue(Object newValue) throws Property.ReadOnlyException {
            if (isReadOnly()) {
                throw new Property.ReadOnlyException();
            }
            try {
                B builder = createNewBuilder();
                builderWriteMethod.invoke(builder, newValue);
                setInternalEntity(entityClass.cast(builder.build()));
                fireValueChangeEvent();
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Could not set value", ex);
            } catch (InvocationTargetException ex) {
                throw unwrap(ex);
            }
        }

        @Override
        public Class getType() {
            return entityReadMethod.getReturnType();
        }

        @Override
        public boolean isReadOnly() {
            return builderWriteMethod == null || getEntity() == null || readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            if (builderWriteMethod == null) {
                throw new UnsupportedOperationException("No write method is available so this property is always read only");
            }
            if (readOnly != newStatus) {
                readOnly = newStatus;
                fireReadOnlyStatusChangeEvent();
            }
        }

        @Override
        public void addValueChangeListener(Property.ValueChangeListener listener) {
            valueChangeListeners.add(listener);
        }

        @Override
        @Deprecated
        public void addListener(Property.ValueChangeListener listener) {
            addValueChangeListener(listener);
        }

        @Override
        public void removeValueChangeListener(Property.ValueChangeListener listener) {
            valueChangeListeners.remove(listener);
        }

        @Override
        @Deprecated
        public void removeListener(Property.ValueChangeListener listener) {
            removeValueChangeListener(listener);
        }

        @Override
        public void addReadOnlyStatusChangeListener(Property.ReadOnlyStatusChangeListener listener) {
            readOnlyStatusChangeListeners.add(listener);
        }

        @Override
        @Deprecated
        public void addListener(Property.ReadOnlyStatusChangeListener listener) {
            addReadOnlyStatusChangeListener(listener);
        }

        @Override
        public void removeReadOnlyStatusChangeListener(Property.ReadOnlyStatusChangeListener listener) {
            readOnlyStatusChangeListeners.remove(listener);
        }

        @Override
        @Deprecated
        public void removeListener(Property.ReadOnlyStatusChangeListener listener) {
            removeReadOnlyStatusChangeListener(listener);
        }

        protected void fireReadOnlyStatusChangeEvent() {
            final Property.ReadOnlyStatusChangeEvent event = new Property.ReadOnlyStatusChangeEvent() {
                @Override
                public Property getProperty() {
                    return BuilderProperty.this;
                }
            };
            for (Property.ReadOnlyStatusChangeListener listener : new HashSet<>(readOnlyStatusChangeListeners)) {
                listener.readOnlyStatusChange(event);
            }
        }

        protected void fireValueChangeEvent() {
            final Property.ValueChangeEvent event = new Property.ValueChangeEvent() {
                @Override
                public Property getProperty() {
                    return BuilderProperty.this;
                }
            };
            for (Property.ValueChangeListener listener : new HashSet<>(valueChangeListeners)) {
                listener.valueChange(event);
            }
        }
    }

    private static RuntimeException unwrap(InvocationTargetException ex) {
        if (ex.getTargetException() instanceof RuntimeException) {
            return (RuntimeException) ex.getTargetException();
        } else {
            return new RuntimeException(ex);
        }
    }
}
