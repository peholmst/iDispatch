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
package net.pkhapps.idispatch.alert.server.adapters.email;

import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailAddressListRepository;
import net.pkhapps.idispatch.alert.server.adapters.email.data.EmailReceiver;
import net.pkhapps.idispatch.alert.server.application.ports.receiver.ReceiverPort;
import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class EmailCourierTest {

    EmailAddressListRepository emailAddressListRepository;
    ReceiverPort receiverPort;
    SmtpGateway smtpGateway;
    AlertTextMessageFormatter alertTextMessageFormatter;
    EmailCourier courier;

    @BeforeEach
    void setUp() {
        emailAddressListRepository = mock(EmailAddressListRepository.class);
        receiverPort = mock(ReceiverPort.class);
        smtpGateway = mock(SmtpGateway.class);
        alertTextMessageFormatter = mock(AlertTextMessageFormatter.class);
        courier = new EmailCourier(emailAddressListRepository, receiverPort, smtpGateway, alertTextMessageFormatter);
    }

    @Test
    void supports_EmailReceiver_returnsTrue() {
        assertThat(courier.supports(EmailReceiver.class)).isTrue();
    }

    @Test
    void deliver_singleReceiver_allOk() {

    }
}
