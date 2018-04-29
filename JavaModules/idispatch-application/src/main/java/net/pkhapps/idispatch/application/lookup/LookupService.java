package net.pkhapps.idispatch.application.lookup;

import org.springframework.lang.NonNull;

import java.util.List;

public interface LookupService<DTO> {

    @NonNull
    List<DTO> findAll();
}
