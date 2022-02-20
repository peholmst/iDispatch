// iDispatch Station Alert
// Copyright (C) 2021, 2022 Petter Holmström
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

#include <boost/log/trivial.hpp>

#include "ServerClient.hpp"

client::ServerClient::ServerClient(const ClientParameters parameters)
    : resolver(ioc), ws(ioc), state(DISCONNECTED), parameters(parameters)
{
}

client::ServerClient::~ServerClient()
{
}

void client::ServerClient::connect()
{
    if (parameters.servers.size() == 0)
    {
        BOOST_LOG_TRIVIAL(warning) << "Cannot connect to a server because there are no servers defined";
    }
    else
    {
        BOOST_LOG_TRIVIAL(info) << "Attempting to connect to a server";
        doSetState(CONNECTING);
        for (auto server : parameters.servers)
        {
            try
            {
                doConnect(server);
                doSetState(CONNECTED);
                return;
            }
            catch (...)
            {
                BOOST_LOG_TRIVIAL(error) << "An error occurred while connecting to server " << server.hostname;
            }
        }
        doSetState(DISCONNECTED);
    }
}

void client::ServerClient::doConnect(const Server &server)
{
    BOOST_LOG_TRIVIAL(info) << "Trying to connect to server " << server.hostname << ":" << server.port;
    auto const results = resolver.resolve(server.hostname, std::to_string(server.port));
    net::connect(ws.next_layer(), results.begin(), results.end());
    ws.handshake(server.hostname, server.contextPath);
}

void client::ServerClient::disconnect()
{
    if (state == CONNECTED)
    {
        doSetState(DISCONNECTING);
        try
        {
            if (ws.is_open())
            {
            }
        }
        catch (...)
        {
        }
    }
}
