package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.boundary.events.AssignmentClosed;
import net.pkhsolutions.idispatch.boundary.events.AssignmentOpened;
import net.pkhsolutions.idispatch.boundary.events.AssignmentUpdated;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.repository.AssignmentRepository;
import net.pkhsolutions.idispatch.utils.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
class AssignmentServiceBean extends AbstractServiceBean implements AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    ResourceStatusService resourceStatusService;

    @Override
    public Optional<Assignment> findAssignment(Long id) {
        logger.debug("Looking up assignment with ID {}", id);
        if (id == null) {
            return Optional.empty();
        }
        final Assignment assignment = assignmentRepository.findOne(id);
        return Optional.ofNullable(assignment);
    }

    @Override
    public Long openAssignment() {
        logger.debug("Opening new assignment");
        final Assignment createdAssignment = getTxTemplate().execute(status -> assignmentRepository.saveAndFlush(new Assignment()));
        getApplicationContext().publishEvent(new AssignmentOpened(this, createdAssignment));
        logger.debug("Opened assignment with ID {}", createdAssignment.getId());
        return createdAssignment.getId();
    }

    @Override
    public UpdateResult<Assignment> updateAssignment(Assignment assignment) {
        logger.debug("Updating assignment {}", assignment);
        if (assignment.isClosed()) {
            logger.debug("Assignment {} is closed, ignoring", assignment);
            return new UpdateResult.NoChange<>(assignment);
        }
        try {
            final Assignment updatedAssignment = getTxTemplate().execute(status -> assignmentRepository.saveAndFlush(assignment));
            getApplicationContext().publishEvent(new AssignmentUpdated(this, updatedAssignment));
            return new UpdateResult.Success<>(updatedAssignment);
        } catch (OptimisticLockingFailureException ex) {
            logger.debug("Assignment {} was updated by another user, cannot update", assignment);
            final Assignment updatedAssignment = assignmentRepository.findOne(assignment.getId());
            return new UpdateResult.Conflict<>(assignment, updatedAssignment);
        }
    }

    @Override
    public boolean closeAssignment(Assignment assignment) {
        return closeAssignment(assignment.getId());
    }

    private boolean closeAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findOne(assignmentId);
        if (assignment == null) {
            logger.debug("Could not close assignment - no such assignment ID: {}", assignmentId);
            return false;
        }
        logger.debug("Closing assignment {}", assignment);
        if (assignment.isClosed()) {
            logger.debug("Assignment {} is already closed, ignoring", assignment);
            return false;
        }
        if (!resourceStatusService.getStatusOfResourcesAssignedToAssignment(assignment).isEmpty()) {
            logger.debug("Resources are still assigned to assignment {}, ignoring", assignment);
            return false;
        }
        assignment.setClosed(new Date());
        final Assignment closedAssignment = getTxTemplate().execute(status -> assignmentRepository.saveAndFlush(assignment));
        resourceStatusService.clearAssignmentFromAllResources(closedAssignment);
        getApplicationContext().publishEvent(new AssignmentClosed(this, closedAssignment));
        return true;
    }

    @Override
    public List<Assignment> findOpenAssignments() {
        logger.debug("Looking up open assignments");
        return assignmentRepository.findByClosedIsNull();
    }
}
