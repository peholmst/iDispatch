package net.pkhapps.idispatch.application.lookup;

import net.pkhapps.idispatch.domain.assignment.AssignmentType;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
class AssignmentTypeLookupServiceImpl implements AssignmentTypeLookupService {

    private final AssignmentTypeRepository assignmentTypeRepository;

    AssignmentTypeLookupServiceImpl(AssignmentTypeRepository assignmentTypeRepository) {
        this.assignmentTypeRepository = assignmentTypeRepository;
    }

    @Override
    public List<AssignmentTypeLookupDTO> findAll() {
        return assignmentTypeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @NonNull
    private AssignmentTypeLookupDTO toDTO(@NonNull AssignmentType assignmentType) {
        return new AssignmentTypeLookupDTO(assignmentType.getId(), assignmentType.getFormattedDescription());
    }
}
