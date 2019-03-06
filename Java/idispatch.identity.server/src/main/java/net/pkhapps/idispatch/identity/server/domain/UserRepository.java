package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for {@link User}.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    Optional<User> findByUsername(@NonNull String username);
}
