package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.ArchivedResourceStatus;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.repository.ArchivedResourceStatusRepository;
import net.pkhsolutions.idispatch.entity.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static net.pkhsolutions.idispatch.boundary.ReportResourceDTO.Builder;

@Service
class ReportServiceBean implements ReportService {

    @Autowired
    ArchivedResourceStatusRepository archivedResourceStatusRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Override
    public Optional<ReportDTO> findReportByAssignmentId(long assignmentId) {
        Assignment assignment = assignmentRepository.findOne(assignmentId);
        if (assignment == null || assignment.isAssignmentOpen()) {
            return Optional.empty();
        } else {
            List<ReportResourceDTO> reportResources = new ReportResourceGenerator(assignment).getReportResources();
            return Optional.of(new ReportDTO(assignment, reportResources));
        }
    }

    @FunctionalInterface
    interface DateSetter {
        void set(Builder owner, Date date);
    }

    @FunctionalInterface
    interface DateGetter {
        Date get(Builder owner);
    }

    private class ReportResourceGenerator {

        private final Assignment assignment;

        private final Map<Resource, Builder> builderMap = new HashMap<>();
        private final List<ReportResourceDTO> dtoList = new ArrayList<>();

        private ReportResourceGenerator(Assignment assignment) {
            this.assignment = assignment;
            process();
        }

        private void process() {
            archivedResourceStatusRepository.findByAssignment(assignment).forEach(this::process);
            dtoList.addAll(builderMap.values().stream().map(Builder::build).collect(Collectors.toList()));
        }

        public List<ReportResourceDTO> getReportResources() {
            return dtoList;
        }

        private void process(ArchivedResourceStatus status) {
            switch (status.getState()) {
                case AT_STATION:
                    process(status, Builder::getAtStation, Builder::setAtStation);
                    break;
                case AVAILABLE:
                    process(status, Builder::getAvailable, Builder::setAvailable);
                    break;
                case DISPATCHED:
                    process(status, Builder::getDispatched, Builder::setDispatched);
                    break;
                case EN_ROUTE:
                    process(status, Builder::getEnRoute, Builder::setEnRoute);
                    break;
                case ON_SCENE:
                    process(status, Builder::getOnScene, Builder::setOnScene);
            }
        }

        private void process(ArchivedResourceStatus status, DateGetter getter, DateSetter setter) {
            Builder builder = getBuilder(status.getResource());
            if (getter.get(builder) == null) {
                setter.set(builder, status.getTimestamp());
            } else {
                dtoList.add(builder.build());
                builderMap.remove(status.getResource());
                process(status, getter, setter);
            }
        }

        private Builder getBuilder(Resource resource) {
            Builder builder = builderMap.get(resource);
            if (builder == null) {
                builder = new Builder(resource);
                builderMap.put(resource, builder);
            }
            return builder;
        }


    }

}
