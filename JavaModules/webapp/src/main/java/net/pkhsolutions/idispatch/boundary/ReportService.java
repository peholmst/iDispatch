package net.pkhsolutions.idispatch.boundary;

import java.util.Optional;

public interface ReportService {

    Optional<ReportDTO> findReportByAssignmentId(long assignmentId);

}
