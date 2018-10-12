package net.pkhapps.idispatch.domain.support;

import java.util.List;

public interface DomainEventLog<E extends DomainEvent> {

    List<DomainEventRecord<E>> events();

    long offset();

    default long indexOfNewestEvent() {
        return offset() + size();
    }

    default long indexOfOldestEvent() {
        return offset();
    }

    default int size() {
        return events().size();
    }

    boolean isCurrent();
}
