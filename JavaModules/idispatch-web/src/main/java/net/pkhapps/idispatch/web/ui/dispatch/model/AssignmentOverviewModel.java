package net.pkhapps.idispatch.web.ui.dispatch.model;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import net.pkhapps.idispatch.application.overview.AssignmentOverviewDTO;
import net.pkhapps.idispatch.application.overview.AssignmentOverviewService;
import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.web.ui.common.AbstractModel;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

/**
 * TODO Document me!
 */
@SpringComponent
@UIScope
public class AssignmentOverviewModel extends AbstractModel<AssignmentOverviewModel.Observer> {

    private final AssignmentOverviewService assignmentOverviewService;
    private final Clock clock;

    private final Map<AssignmentId, AssignmentOverviewDTO> assignmentOverviewMap = new HashMap<>();
    private Instant lastRefresh;


    AssignmentOverviewModel(AssignmentOverviewService assignmentOverviewService, Clock clock) {
        this.assignmentOverviewService = assignmentOverviewService;
        this.clock = clock;
        refresh();
    }

    /**
     *
     */
    public void refresh() {
        if (lastRefresh == null) {
            assignmentOverviewMap.clear();
            assignmentOverviewService.getAll().forEach(dto -> assignmentOverviewMap.put(dto.getAssignmentId(), dto));
            notifyObservers(observer -> observer.onAllAssignmentsChanged(this));
        } else {
            assignmentOverviewService.getChangesSince(lastRefresh).forEach(dto -> {
                assignmentOverviewMap.put(dto.getAssignmentId(), dto);
                notifyObservers(observer -> observer.onSingleAssignmentChanged(this, dto));
            });
        }
        lastRefresh = clock.instant();
    }

    @NonNull
    public Set<AssignmentId> getAssignments() {
        return new HashSet<>(assignmentOverviewMap.keySet());
    }

    @NonNull
    public Optional<AssignmentOverviewDTO> getAssignmentOverview(@NonNull AssignmentId assignmentId) {
        return Optional.ofNullable(assignmentOverviewMap.get(assignmentId));
    }

    /**
     *
     */
    public interface Observer extends Serializable {
        default void onAllAssignmentsChanged(@NonNull AssignmentOverviewModel model) {
            // NOP
        }

        default void onSingleAssignmentChanged(@NonNull AssignmentOverviewModel model,
                                               @NonNull AssignmentOverviewDTO changedAssignment) {
            // NOP
        }
    }
}
