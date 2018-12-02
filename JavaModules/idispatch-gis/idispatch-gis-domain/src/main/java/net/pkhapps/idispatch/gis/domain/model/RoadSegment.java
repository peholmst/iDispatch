package net.pkhapps.idispatch.gis.domain.model;

import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.gis.domain.model.identity.RoadSegmentId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "road_segment", schema = "gis")
@SequenceGenerator(name = "roadSegmentId", sequenceName = "road_segment_id_seq", schema = "gis")
public class RoadSegment extends ImportedGeographicalMaterial<Long, RoadSegmentId> {

    private static final int ROAD_NUMBER_MAX_LENGTH = 50;
    private static final int NAME_MAX_LENGTH = 200;

    @Column(name = "gid", nullable = false)
    private Long gid;

    @Column(name = "location_accuracy", nullable = false)
    private Integer locationAccuracy; // TODO Replace with enum

    @Column(name = "location", nullable = false)
    private LineString location;

    @Column(name = "elevation", nullable = false)
    private Integer elevation; // TODO Replace with enum

    @Column(name = "road_number", length = ROAD_NUMBER_MAX_LENGTH)
    private String roadNumber;

    @Column(name = "name_fin", length = NAME_MAX_LENGTH)
    private String nameFin;

    @Column(name = "name_swe", length = NAME_MAX_LENGTH)
    private String nameSwe;

    @Column(name = "municipality_id", nullable = false)
    private MunicipalityId municipality;

    @Column(name = "min_addr_number_left")
    private Integer minAddressNumberLeft;

    @Column(name = "max_addr_number_left")
    private Integer maxAddressNumberLeft;

    @Column(name = "min_addr_number_right")
    private Integer minAddressNumberRight;

    @Column(name = "max_addr_number_right")
    private Integer maxAddressNumberRight;

}
