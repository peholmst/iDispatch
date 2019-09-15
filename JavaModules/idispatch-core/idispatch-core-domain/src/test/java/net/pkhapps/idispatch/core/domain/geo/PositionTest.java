package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObjectTestBase;
import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Position}.
 */
public class PositionTest extends ValueObjectTestBase<Position> {

    @Override
    protected Position createTypical() {
        return Position.createFromTm35Fin(240478, 6694821);
    }

    @Test
    public void toWgs84_convertedCoordinatesCorrespondToSamePlaceOnEarth() {
        var position = createTypical();
        var wgs84 = position.toWgs84();
        assertThat(wgs84.getCoordinateReferenceSystem()).isEqualTo(CoordinateReferenceSystems.WGS84);
        assertThat(wgs84.getX()).isEqualTo(22.30098526, Offset.offset(0.00000001));
        assertThat(wgs84.getY()).isEqualTo(60.30670076, Offset.offset(0.00000001));
    }

    @Override
    public void initialState_newlyCreatedValueObjectIsInValidState() {
        var position = createTypical();
        var directPosition = position.toTm35Fin();
        assertThat(directPosition.getCoordinateReferenceSystem()).isEqualTo(CoordinateReferenceSystems.TM35FIN);
        assertThat(directPosition.getX()).isEqualTo(240478);
        assertThat(directPosition.getY()).isEqualTo(6694821);
    }

    @Override
    public void equals_valueObjectIsNotEqualToDifferentValueObjectOfSameType() {
        var first = createTypical();
        var second = Position.createFromTm35Fin(240479, 6694822);
        assertThat(first).isNotEqualTo(second);
        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }
}
