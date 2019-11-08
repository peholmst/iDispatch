package net.pkhapps.idispatch.dws.ui.components;

import com.vaadin.flow.component.customfield.CustomField;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class MunicipalityField extends CustomField<MunicipalityCode> {

    private final MunicipalityLookupService municipalityLookupService;

    public MunicipalityField(@NotNull MunicipalityLookupService municipalityLookupService) {
        this.municipalityLookupService = requireNonNull(municipalityLookupService);
    }

    public @NotNull MunicipalityField withLabel(@Nullable String label) {
        setLabel(label);
        return this;
    }

    @Override
    protected MunicipalityCode generateModelValue() {
        return null;
    }

    @Override
    protected void setPresentationValue(MunicipalityCode municipalityCode) {

    }
}
