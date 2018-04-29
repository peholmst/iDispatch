package net.pkhapps.idispatch.web.ui.dispatch.model;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.Registration;
import net.pkhapps.idispatch.application.overview.AssignmentOverviewDTO;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Optional;

/**
 * TODO Document me!
 */
public class AssignmentOverviewDataProvider extends ListDataProvider<AssignmentOverviewDTO>
        implements AssignmentOverviewModel.Observer {

    private Registration modelRegistration;

    public AssignmentOverviewDataProvider() {
        super(new HashSet<>());
    }

    public void registerWithModel(@NonNull AssignmentOverviewModel model) {
        unregisterFromModel();
        modelRegistration = model.addObserver(this);
        onAllAssignmentsChanged(model);
    }

    public void unregisterFromModel() {
        if (modelRegistration != null) {
            modelRegistration.remove();
        }
    }

    @Override
    public Object getId(AssignmentOverviewDTO item) {
        return item.getAssignmentId();
    }

    @Override
    public void onAllAssignmentsChanged(@NonNull AssignmentOverviewModel model) {
        getItems().clear();
        model.getAssignments().stream()
                .map(model::getAssignmentOverview)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(getItems()::add);
        refreshAll();
    }

    @Override
    public void onSingleAssignmentChanged(@NonNull AssignmentOverviewModel model,
                                          @NonNull AssignmentOverviewDTO changedAssignment) {
        getItems().removeIf(dto -> dto.getAssignmentId().equals(changedAssignment.getAssignmentId()));
        getItems().add(changedAssignment);
        refreshItem(changedAssignment);
    }
}
