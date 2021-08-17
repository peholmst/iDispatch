package net.pkhapps.idispatch.alert.server.application.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import net.pkhapps.idispatch.alert.server.application.security.SecurityManager;
import net.pkhapps.idispatch.alert.server.domain.infra.UnitOfWorkManager;
import net.pkhapps.idispatch.alert.server.domain.model.AlertId;

/**
 * Default implementation of {@link DispatcherPort} that enforces security and
 * manages units of work but otherwise delegates to {@link AlertCommandHandler}
 * and {@link AlertStatusHandler}.
 */
class DispatcherPortImpl implements DispatcherPort {

    private final UnitOfWorkManager unitOfWorkManager;
    private final SecurityManager securityManager;
    private final AlertCommandHandler alertCommandHandler;
    private final AlertStatusHandler alertStatusHandler;

    DispatcherPortImpl(UnitOfWorkManager unitOfWorkManager, SecurityManager securityManager,
            AlertCommandHandler alertCommandHandler, AlertStatusHandler alertStatusHandler) {
        this.unitOfWorkManager = requireNonNull(unitOfWorkManager, "unitOfWorkManager must not be null");
        this.securityManager = requireNonNull(securityManager, "securityManager must not be null");
        this.alertCommandHandler = requireNonNull(alertCommandHandler, "alertCommandHandler must not be null");
        this.alertStatusHandler = requireNonNull(alertStatusHandler, "alertStatusHandler must not be null");
    }

    @Override
    public AlertId alert(AlertCommand command) {
        securityManager.checkPermission(DispatcherPermission.SEND_ALERT);
        return unitOfWorkManager.performUnitOfWork(() -> alertCommandHandler.alert(command));
    }

    @Override
    public Optional<AlertStatus> getStatus(AlertId alertId) {
        securityManager.checkPermission(DispatcherPermission.CHECK_ALERT_STATUS);
        return unitOfWorkManager.performUnitOfWork(() -> alertStatusHandler.getStatus(alertId));
    }
}
