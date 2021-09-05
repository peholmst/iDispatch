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

import net.pkhapps.idispatch.alert.server.application.ports.receiver.Courier;
import net.pkhapps.idispatch.alert.server.data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlertDistributorTest {

    private ReceiverRepository receiverRepository;
    private AlertReceiptRepository alertReceiptRepository;
    private Clock clock;
    private AlertDistributor alertDistributor;
    private Executor executor;

    @BeforeEach
    void setUp() {
        receiverRepository = mock(ReceiverRepository.class);
        alertReceiptRepository = mock(AlertReceiptRepository.class);
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        executor = command -> command.run();
        alertDistributor = new AlertDistributor(receiverRepository, alertReceiptRepository, clock, executor);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(alertReceiptRepository);
    }

    @Test
    void distribute_successfulDistribution() {
        var courier = new TestCourier();
        var alert = AlertTestData.createTestAlert();
        var receivers = createTestReceivers();
        var receiverIds = receivers.stream().map(Receiver::id).collect(Collectors.toSet());
        when(receiverRepository.findEnabledByResources(alert.resourcesToAlert())).thenReturn(receivers.stream());
        alertDistributor.registerCourier(courier);
        alertDistributor.distribute(alert);
        verify(alertReceiptRepository).sendingAlertsToReceivers(alert.id(), clock.instant(), receiverIds);
        assertThat(courier.deliveredAlerts).containsEntry(alert, receivers);
    }

    @Test
    void distribute_noCourierFound() {
        var alert = AlertTestData.createTestAlert();
        var receivers = createTestReceivers();
        var receiverIds = receivers.stream().map(Receiver::id).collect(Collectors.toSet());
        when(receiverRepository.findEnabledByResources(alert.resourcesToAlert())).thenReturn(receivers.stream());
        alertDistributor.distribute(alert);
        verify(alertReceiptRepository).couldNotSendAlertToReceivers(alert.id(), clock.instant(), receiverIds,
                AlertDistributor.NON_EXISTENT_COURIER_MSG);
    }

    @Test
    void distribute_errorInCourier() {
        var courier = new FailingTestCourier();
        var alert = AlertTestData.createTestAlert();
        var receivers = createTestReceivers();
        var receiverIds = receivers.stream().map(Receiver::id).collect(Collectors.toSet());
        when(receiverRepository.findEnabledByResources(alert.resourcesToAlert())).thenReturn(receivers.stream());
        alertDistributor.registerCourier(courier);
        alertDistributor.distribute(alert);
        verify(alertReceiptRepository).sendingAlertsToReceivers(alert.id(), clock.instant(), receiverIds);
        verify(alertReceiptRepository).couldNotSendAlertToReceivers(alert.id(), clock.instant(), receiverIds,
                FailingTestCourier.ERROR_MSG);
    }

    @Test
    void unregisterCourier_noCourierFound() {
        var courier = new TestCourier();
        var alert = AlertTestData.createTestAlert();
        var receivers = createTestReceivers();
        var receiverIds = receivers.stream().map(Receiver::id).collect(Collectors.toSet());
        when(receiverRepository.findEnabledByResources(alert.resourcesToAlert())).thenReturn(receivers.stream());
        alertDistributor.registerCourier(courier);
        alertDistributor.unregisterCourier(courier);
        alertDistributor.distribute(alert);
        verify(alertReceiptRepository).couldNotSendAlertToReceivers(alert.id(), clock.instant(), receiverIds,
                AlertDistributor.NON_EXISTENT_COURIER_MSG);
        assertThat(courier.deliveredAlerts).isEmpty();
    }

    private List<Receiver> createTestReceivers() {
        return List.of((Receiver) new TestReceiver(), new TestReceiver());
    }

    static class TestReceiver extends Receiver {

        TestReceiver() {
            super(ReceiverId.randomReceiverId(), true, Collections.emptySet());
        }
    }

    static class TestCourier implements Courier {

        final Map<Alert, Collection<Receiver>> deliveredAlerts = new HashMap<>();

        @Override
        public boolean supports(Class<? extends Receiver> receiverClass) {
            return TestReceiver.class.equals(receiverClass);
        }

        @Override
        public void deliver(Alert alert, Collection<Receiver> receivers) {
            deliveredAlerts.put(alert, receivers);
        }
    }

    static class FailingTestCourier extends TestCourier {

        static final String ERROR_MSG = "This is an expected error";

        @Override
        public void deliver(Alert alert, Collection<Receiver> receivers) {
            throw new RuntimeException(ERROR_MSG);
        }
    }
}
