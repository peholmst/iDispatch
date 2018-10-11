package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Municipality}-entities.
 */
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    List<Municipality> findByActiveTrueOrderByNameAsc();
}
