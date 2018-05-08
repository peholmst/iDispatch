package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.status.ResourceStateChangedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Domain event listener that listens to {@link ResourceStateChangedEvent}s and updates the corresponding
 * {@link AssignmentResource}s for all currently open {@link Assignment}s.
 */
@Service
class AssignmentResourceStateUpdateHandler {

    final AssignmentRepository assignmentRepository;

    AssignmentResourceStateUpdateHandler(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onResourceStateChangedEvent(@NonNull ResourceStateChangedEvent event) {
        var openAssignments = assignmentRepository.findOpenAssignmentsForResource(event.getResource());
        openAssignments.forEach(assignment -> assignment.updateResourceStateIfApplicable(event.getResource(),
                event.getNewState(), event.getTimestamp()));
        assignmentRepository.saveAll(openAssignments);
    }
}
