package net.pkhapps.idispatch.web.ui.common;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;
import net.pkhapps.idispatch.application.lookup.AbstractLookupDTO;
import net.pkhapps.idispatch.application.lookup.LookupService;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @param <ID>
 * @param <DTO>
 */
public abstract class AbstractLookupComboBox<ID, DTO extends AbstractLookupDTO<ID>> extends ComboBox<ID> {

    public AbstractLookupComboBox(@NonNull LookupService<DTO> lookupService) {
        Objects.requireNonNull(lookupService);
        final Map<ID, String> captionMap = new HashMap<>();
        final ListDataProvider<ID> dataProvider = new ListDataProvider<>(new ArrayList<>());
        setDataProvider(dataProvider);
        setItemCaptionGenerator(captionMap::get);
        lookupService.findAll().forEach(dto -> {
            captionMap.put(dto.getId(), dto.getDisplayName());
            dataProvider.getItems().add(dto.getId());
        });
    }
}
