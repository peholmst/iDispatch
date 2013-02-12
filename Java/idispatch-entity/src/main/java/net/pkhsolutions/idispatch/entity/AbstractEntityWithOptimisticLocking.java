package net.pkhsolutions.idispatch.entity;


import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class AbstractEntityWithOptimisticLocking extends AbstractEntity {

    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }
}
