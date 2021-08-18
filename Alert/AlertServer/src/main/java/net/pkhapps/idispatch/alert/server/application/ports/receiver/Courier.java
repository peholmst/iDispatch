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
package net.pkhapps.idispatch.alert.server.application.ports.receiver;

import java.util.Collection;

import net.pkhapps.idispatch.alert.server.domain.model.Alert;
import net.pkhapps.idispatch.alert.server.domain.model.Receiver;

/**
 * SPI interface for a courier that knows how to deliver {@link Alert}s to
 * specific types (classes) of {@link Receiver}s. For example, one courier could
 * deliver an alert message to the resources through SMS, another through
 * e-mail, a thid might deliver alerts to mobile phone apps or station alert
 * systems.
 */
public interface Courier {

    /**
     * Checks whether this courier supports delivering alerts to receivers of the
     * given class.
     * 
     * @param receiverClass the class of the potential receivers to deliver alerts
     *                      to.
     * @return true if the receiver class is supported, false otherwise.
     */
    boolean supports(Class<? extends Receiver> receiverClass);

    /**
     * Delivers the given alert to the given receivers.
     * 
     * @param alert     the alert to deliver.
     * @param receivers the receivers to deliver to. All receivers in this list can
     *                  be assumed to be {@linkplain #supports(Class) supported} by
     *                  this courier.
     */
    void deliver(Alert alert, Collection<Receiver> receivers);
}
