package net.pkhapps.idispatch.application.assignment;

import net.pkhapps.idispatch.domain.assignment.Assignment;
import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Optional;

@Service
class AssignmentServiceImpl implements AssignmentService {

    private final Clock clock;
    private final AssignmentRepository assignmentRepository;

    AssignmentServiceImpl(Clock clock, AssignmentRepository assignmentRepository) {
        this.clock = clock;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void closeAssignment(@NonNull AssignmentId assignmentId) {
        assignmentRepository
                .findById(assignmentId)
                .filter(Assignment::isOpen)
                .ifPresent(ass -> ass.close(clock));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @NonNull
    public AssignmentId openAssignment() {
        return assignmentRepository.saveAndFlush(new Assignment(clock)).getId();
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<AssignmentDetailsDTO> findAssignmentDetails(@NonNull AssignmentId assignmentId) {
        return assignmentRepository.findById(assignmentId).map(this::toDetailsDTO);
    }

    @NonNull
    private AssignmentDetailsDTO toDetailsDTO(@NonNull Assignment assignment) {
        AssignmentDetailsDTO dto = new AssignmentDetailsDTO(assignment.getId(), assignment.getVersion(),
                assignment.getOpened(), assignment.getClosed(), assignment.getState());
        dto.setAddress(assignment.getAddress());
        dto.setType(assignment.getType());
        dto.setDescription(assignment.getDescription());
        dto.setMunicipality(assignment.getMunicipality());
        dto.setPriority(assignment.getPriority());
        return dto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AssignmentUpdatedInOtherSessionException.class)
    public void updateDetailsOfOpenAssignment(AssignmentDetailsDTO updatedDetails)
            throws AssignmentUpdatedInOtherSessionException {
        // TODO Implement me
    }
}
