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
package net.pkhapps.idispatch.alert.server.domain.infra;

import java.util.function.Supplier;

/**
 * Interface defining a unit-of-work manager that is used by the application
 * layer to implement units-of-work. A unit-of-work is like a transaction;
 * either all the work in the unit gets done (the transaction is committed) or
 * none of it gets done (the transaction is rolled back).
 */
public interface UnitOfWorkManager {

    /**
     * Performs the given unit-of-work and returns its result.
     * 
     * @param <T>        the type of the result.
     * @param unitOfWork the unit-of-work to perform, must not be {@code null}.
     * @return the result returned by the unit-of-work, may be {@code null}.
     */
    <T> T performUnitOfWork(Supplier<T> unitOfWork);

    /**
     * Performs the given unit-of-work, discarding its result.
     * 
     * @param unitOfWork the unit-of-work to perform, must not be {@code null}.
     */
    default void performUnitOfWorkWithoutResult(Runnable unitOfWork) {
        performUnitOfWork(() -> {
            unitOfWork.run();
            return null;
        });
    }
}
