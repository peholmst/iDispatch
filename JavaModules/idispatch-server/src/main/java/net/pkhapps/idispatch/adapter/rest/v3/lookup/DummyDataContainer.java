package net.pkhapps.idispatch.adapter.rest.v3.lookup;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class used to hold dummy data that is used while prototyping the REST resources. Will be deleted when the real
 * application is finished.
 */
abstract class DummyDataContainer<ID extends DomainObjectId, T extends IdentifiableDomainObject<ID>> {

    private AtomicLong nextFreeId = new AtomicLong(1L);

    private Map<ID, T> dummyData = new HashMap<>();

    T getById(ID id) {
        return dummyData.get(id);
    }

    final Long nextFreeId() {
        return nextFreeId.getAndIncrement();
    }

    final void add(T dummyObject) {
        dummyData.put(dummyObject.id(), dummyObject);
    }

    List<T> all() {
        return List.copyOf(dummyData.values());
    }
}
