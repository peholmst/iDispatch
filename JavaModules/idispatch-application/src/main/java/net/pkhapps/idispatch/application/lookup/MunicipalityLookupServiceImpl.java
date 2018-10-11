package net.pkhapps.idispatch.application.lookup;

import net.pkhapps.idispatch.domain.common.Municipality;
import net.pkhapps.idispatch.domain.common.MunicipalityRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
class MunicipalityLookupServiceImpl implements MunicipalityLookupService {

    private final MunicipalityRepository municipalityRepository;

    MunicipalityLookupServiceImpl(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    @Override
    public List<MunicipalityLookupDTO> findAll() {
        return municipalityRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @NonNull
    private MunicipalityLookupDTO toDTO(@NonNull Municipality municipality) {
        return new MunicipalityLookupDTO(municipality.getId(), municipality.getName());
    }
}
