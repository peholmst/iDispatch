package net.pkhapps.idispatch.core.domain.common;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO Document me!
 *
 * @param <T>
 */
public abstract class ValueObjectTestBase<T extends ValueObject> {

    protected abstract T createTypical();

    @Test
    public void equals_valueObjectIsEqualToSelf() {
        var vo = createTypical();
        assertThat(vo).isEqualTo(vo);
    }

    @Test
    public void equals_valueObjectIsNotEqualToObjectOfDifferentType() {
        var vo = createTypical();
        assertThat(vo).isNotEqualTo("just a string");
    }

    @Test
    public void equals_valueObjectIsNotEqualToNull() {
        var vo = createTypical();
        assertThat(vo).isNotEqualTo(null);
    }

    @Test
    public void equals_valueObjectIsEqualToCopyOfItSelf() {
        var vo1 = createTypical();
        var vo2 = createTypical();
        assertThat(vo1).isEqualTo(vo2);
        assertThat(vo1).hasSameHashCodeAs(vo2);
    }

    @Test
    public abstract void initialState_newlyCreatedValueObjectIsInValidState();

    @Test
    public abstract void equals_valueObjectIsNotEqualToDifferentValueObjectOfSameType();
}
