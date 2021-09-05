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
 * A mock implementation of {@link UnitOfWorkManager} designed to be used in
 * unit tests.
 */
public class MockUnitOfWorkManager implements UnitOfWorkManager {

    private Object lastUnitOfWorkResult;

    @Override
    public <T> T performUnitOfWork(Supplier<T> unitOfWork) {
        final var result = unitOfWork.get();
        this.lastUnitOfWorkResult = result;
        return result;
    }

    /**
     * Gets the result of the last performed unit of work.
     *
     * @return the result, may be {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> T getLastUnitOfWorkResult() {
        return (T) lastUnitOfWorkResult;
    }
}
