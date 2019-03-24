package net.pkhapps.idispatch.identity.server.domain;

import net.pkhapps.idispatch.base.domain.Repository;
import net.pkhapps.idispatch.base.domain.UserId;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for {@link User}.
 */
public interface UserRepository extends Repository<User, UserId> {

    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    boolean existsByUsername(@NonNull String username);
}
