// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.pkhapps.idispatch.alert.server.application;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.pkhapps.idispatch.alert.server.application.ports.receiver.Courier;
import net.pkhapps.idispatch.alert.server.domain.model.Alert;
import net.pkhapps.idispatch.alert.server.domain.model.AlertReceiptRepository;
import net.pkhapps.idispatch.alert.server.domain.model.Receiver;
import net.pkhapps.idispatch.alert.server.domain.model.ReceiverRepository;

/**
 * This component is responsible for distributing {@link Alert}s to
 * {@link Receiver}s through different {@link Courier}s. Couriers can be
 * dynamically registered and removed.
 */
class AlertDistributor {

    private static final Logger log = LoggerFactory.getLogger(AlertDistributor.class);
    private static final Courier NON_EXISTENT_COURIER = new Courier() {

        @Override
        public boolean supports(Class<? extends Receiver> receiverClass) {
            return false;
        }

        @Override
        public void deliver(Alert alert, Collection<Receiver> receivers) {
            // NOP
        }
    };
    static final String NON_EXISTENT_COURIER_MSG = "No courier found";
    private final ReadWriteLock couriersLock = new ReentrantReadWriteLock();
    private final Set<Courier> couriers = new HashSet<>();
    private final ReceiverRepository receiverRepository;
    private final AlertReceiptRepository alertReceiptRepository;
    private final Clock clock;
    private final Executor alertDeliveryExecutor;

    /**
     * Creates a new {@code AlertDistributor}.
     * 
     * @param receiverRepository     the repository to use for looking up
     *                               {@link Receiver}s to distribute alerts to.
     * @param alertReceiptRepository the repository to use for storing the result of
     *                               alert distribution attempts.
     * @param clock                  the clock to use for getting the current date
     *                               and time.
     * @param alertDeliveryExecutor  the {@link Executor} to use when delivering
     *                               alerts through {@link Courier}s.
     */
    AlertDistributor(ReceiverRepository receiverRepository, AlertReceiptRepository alertReceiptRepository, Clock clock,
            Executor alertDeliveryExecutor) {
        this.receiverRepository = requireNonNull(receiverRepository, "receiverRepository cannot be null");
        this.alertReceiptRepository = requireNonNull(alertReceiptRepository, "alertReceiptRepository cannot be null");
        this.clock = requireNonNull(clock, "clock must not be null");
        this.alertDeliveryExecutor = requireNonNull(alertDeliveryExecutor, "alertDeliveryExecutor cannot be null");
    }

    /**
     * Registers the given courier with this distributor. If the same courier has
     * already been registered, nothing happens.
     * 
     * @param courier the courier to register.
     */
    void registerCourier(Courier courier) {
        requireNonNull(courier, "courier cannot be null");
        log.info("Registering courier {}", courier);
        couriersLock.writeLock().lock();
        try {
            couriers.add(courier);
        } finally {
            couriersLock.writeLock().unlock();
        }
    }

    /**
     * Unregisters the given courier from this distributor. No alerts will be
     * distributed through it after this. If the courier had not been registered in
     * the first place (or was already unregistered), nothing happens.
     * 
     * @param courier the courier to unregister.
     */
    void unregisterCourier(Courier courier) {
        requireNonNull(courier, "courier cannot be null");
        log.info("Unregistering courier {}", courier);
        couriersLock.writeLock().lock();
        try {
            couriers.remove(courier);
        } finally {
            couriersLock.writeLock().unlock();
        }
    }

    /**
     * Distributes the given alert to its {@link Receiver}s.
     * 
     * @param alert the alert to distribute.
     */
    void distribute(Alert alert) {
        requireNonNull(alert, "alert cannot be null");
        log.info("Starting delivery of alert [{}]", alert);
        receiverRepository.findByResources(alert.resourcesToAlert())
                .collect(Collectors.groupingBy(this::findCourierOfReceiver, HashMap::new, Collectors.toList()))
                .forEach((courier, receivers) -> alertDeliveryExecutor
                        .execute(() -> deliver(courier, alert, receivers)));
        log.info("Delivery of alert [{}] has been started", alert);
    }

    private Courier findCourierOfReceiver(Receiver receiver) {
        couriersLock.readLock().lock();
        try {
            return couriers.stream().filter(c -> c.supports(receiver.getClass())).findFirst()
                    .orElse(NON_EXISTENT_COURIER);
        } finally {
            couriersLock.readLock().unlock();
        }
    }

    private void deliver(Courier courier, Alert alert, Collection<Receiver> receivers) {
        if (courier == NON_EXISTENT_COURIER) {
            log.warn("No courier was found to deliver alert [{}] to {} receivers", alert, receivers.size());
            alertReceiptRepository.couldNotSendAlertToReceivers(alert.id(), clock.instant(), receivers,
                    NON_EXISTENT_COURIER_MSG);
        } else {
            log.info("Delivering alert [{}] through courier [{}] to {} receivers", alert, courier, receivers.size());
            alertReceiptRepository.sendingAlertsToReceivers(alert.id(), clock.instant(), receivers);
            try {
                courier.deliver(alert, receivers);
            } catch (Exception ex) {
                log.error("Error delivering alert [" + alert + "] through courier [" + courier + "]", ex);
                alertReceiptRepository.couldNotSendAlertToReceivers(alert.id(), clock.instant(), receivers,
                        ex.getMessage());
            }
        }
    }
}
