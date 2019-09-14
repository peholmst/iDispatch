package net.pkhapps.idispatch.core.domain.i18n;

import net.pkhapps.idispatch.core.domain.common.ValueObjectTestBase;
import org.testng.annotations.Test;

import javax.sound.midi.MidiChannel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link MultilingualString}.
 */
public class MultilingualStringTest extends ValueObjectTestBase<MultilingualString> {

    @Override
    protected MultilingualString createTypical() {
        return new MultilingualString(Locales.SWEDISH, "hejsan");
    }

    @Override
    public void initialState_newlyCreatedValueObjectIsInValidState() {
        var s = createTypical();
        assertThat(s.toMap()).containsEntry(Locales.SWEDISH, "hejsan");
    }

    @Override
    public void equals_valueObjectIsNotEqualToDifferentValueObjectOfSameType() {
        var s = createTypical();
        var s2 = new MultilingualString(Locales.FINNISH, "terve");
        assertThat(s).isNotEqualTo(s2);
        assertThat(s.hashCode()).isNotEqualTo(s2.hashCode());
    }
}
