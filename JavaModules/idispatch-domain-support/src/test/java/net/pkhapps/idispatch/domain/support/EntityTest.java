package net.pkhapps.idispatch.domain.support;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Entity}.
 */
public class EntityTest {

    @Test
    public void equalsAndHashCode_entitiesHaveDifferentIds_entitiesAreNotEqual() {
        var entity1 = new TestEntity(new TestEntityId(1));
        var entity2 = new TestEntity(new TestEntityId(2));
        assertThat(entity1).isNotEqualTo(entity2);
        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    public void equalsAndHashCode_entitiesHaveSameId_entitiesAreEqual() {
        var entity1 = new TestEntity(new TestEntityId(1));
        var entity2 = entity1.clone();
        assertThat(entity1).isNotSameAs(entity2);
        assertThat(entity1).isEqualTo(entity2);
        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    private static class TestEntityId extends DomainObjectId {
        TestEntityId(@Nonnull Serializable id) {
            super(id);
        }
    }

    private static class TestEntity extends Entity<TestEntityId> {
        TestEntity(@Nonnull TestEntityId id) {
            super(id);
        }
    }
}
