package net.pkhsolutions.idispatch.dws.ui.data;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.lang.reflect.InvocationTargetException;
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
public class BuilderItem<E extends AbstractEntity, B extends AbstractEntity.AbstractEntityBuilder> implements Item {

    private Class<E> entityClass;
    private Class<B> builderClass;
    // TODO Serialize classes
    private Map<Object, BuilderProperty> propertyMap = new HashMap<>();
    private E entity;

    public BuilderItem(Class<E> entityClass, Class<B> builderClass) {
        this.entityClass = entityClass;
        this.builderClass = builderClass;
        registerProperties(BuilderPropertyDescriptor.sanForProperties(entityClass, builderClass));
    }

    protected BuilderItem(Class<E> entityClass, Class<B> builderClass, Collection<BuilderPropertyDescriptor<E, B>> propertyDescriptors) {
        this.entityClass = entityClass;
        this.builderClass = builderClass;
        registerProperties(propertyDescriptors);
    }

    private void registerProperties(Collection<BuilderPropertyDescriptor<E, B>> propertyDescriptors) {
        for (BuilderPropertyDescriptor<E, B> descriptor : propertyDescriptors) {
            propertyMap.put(descriptor.getName(), new BuilderProperty(descriptor));
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
            throw ExceptionUtils.unwrap(ex);
        }
    }

    public class BuilderProperty implements Property, Property.ValueChangeNotifier, Property.ReadOnlyStatusChangeNotifier {

        private BuilderPropertyDescriptor<E, B> descriptor;
        private boolean readOnly;
        private Set<Property.ValueChangeListener> valueChangeListeners = new HashSet<>();
        private Set<ReadOnlyStatusChangeListener> readOnlyStatusChangeListeners = new HashSet<>();

        protected BuilderProperty(BuilderPropertyDescriptor<E, B> descriptor) {
            this.descriptor = descriptor;
        }

        @Override
        public Object getValue() {
            return descriptor.getValue(getEntity());
        }

        @Override
        public void setValue(Object newValue) throws Property.ReadOnlyException {
            if (isReadOnly()) {
                throw new Property.ReadOnlyException();
            }
            B builder = createNewBuilder();
            E newEntity = descriptor.setValue(builder, newValue);
            setInternalEntity(newEntity);
            fireValueChangeEvent();
        }

        @Override
        public Class getType() {
            return descriptor.getType();
        }

        @Override
        public boolean isReadOnly() {
            return !descriptor.isWritable() || getEntity() == null || readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            if (!descriptor.isWritable()) {
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
}
