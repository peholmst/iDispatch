package net.pkhsolutions.idispatch.dws.ui.data;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractInMemoryContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.pkhsolutions.idispatch.entity.AbstractEntity;

/**
 *
 * @author Petter Holmström
 * @param <E>
 * @param <B>
 */
public class BuilderItemContainer<E extends AbstractEntity, B extends AbstractEntity.AbstractEntityBuilder>
        extends AbstractInMemoryContainer<UUID, String, BuilderItem<E, B>>
        implements Container.Filterable, Container.Sortable {

    private Map<Object, BuilderPropertyDescriptor<E, B>> propertyDescriptors = new HashMap<>();
    private Map<Object, BuilderItem<E, B>> items = new HashMap<>();
    private Class<E> entityClass;
    private Class<B> builderClass;
    // TODO Serialize classes

    public BuilderItemContainer(Class<E> entityClass, Class<B> builderClass) {
        this.entityClass = entityClass;
        this.builderClass = builderClass;
        for (BuilderPropertyDescriptor<E, B> descriptor : BuilderPropertyDescriptor.sanForProperties(entityClass, builderClass)) {
            propertyDescriptors.put(descriptor.getName(), descriptor);
        }
    }

    public UUID addEntity(E entity) {
        BuilderItem<E, B> item = new BuilderItem<>(entityClass, builderClass, propertyDescriptors.values());
        item.setEntity(entity);
        UUID uuid = UUID.randomUUID();
        internalAddItemAtEnd(uuid, item, true);
        return uuid;
    }

    public void addEntities(Collection<E> entities) {
        for (E entity : entities) {
            BuilderItem<E, B> item = new BuilderItem<>(entityClass, builderClass, propertyDescriptors.values());
            item.setEntity(entity);
            UUID uuid = UUID.randomUUID();
            internalAddItemAtEnd(uuid, item, false);
        }
        if (!isFiltered()) {
            fireItemSetChange();
        } else {
            filterAll();
        }
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        internalRemoveItem(itemId);
        items.remove(itemId);
        fireItemSetChange();
        return true;
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        internalRemoveAllItems();
        items.clear();
        fireItemSetChange();
        return true;
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.unmodifiableSet(propertyDescriptors.keySet());
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        BuilderItem<E, B> item = getUnfilteredItem(itemId);
        if (item == null) {
            return null;
        } else {
            return item.getItemProperty(propertyId);
        }
    }

    @Override
    public Class<?> getType(Object propertyId) {
        BuilderPropertyDescriptor<E, B> descriptor = propertyDescriptors.get(propertyId);
        if (descriptor == null) {
            return null;
        } else {
            return descriptor.getType();
        }
    }

    @Override
    public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
        addFilter(filter);
    }

    @Override
    public void removeContainerFilter(Filter filter) {
        removeFilter(filter);
    }

    @Override
    public void removeAllContainerFilters() {
        removeAllFilters();
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {
        sortContainer(propertyId, ascending);
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        return getContainerPropertyIds();
    }

    @Override
    protected BuilderItem<E, B> getUnfilteredItem(Object itemId) {
        return items.get(itemId);
    }

    @Override
    protected void registerNewItem(int position, UUID itemId, BuilderItem<E, B> item) {
        items.put(itemId, item);
    }
}
