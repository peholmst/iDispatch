// iDispatch Station Alert
// Copyright (C) 2022 Petter Holmström
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

#include "Connection.hpp"

#include <boost/beast/core.hpp>

constexpr auto SSL_ROOT_CERTIFICATES_FILE = "/etc/ssl/certs/ca-certificates.crt";

using namespace idispatch::net;

Connection::Connection(const std::string &host, const std::string &service, const uint16_t &connectionTimeoutMs) : host(host), service(service), connectionTimeoutMs(connectionTimeoutMs)
{
    state.store(ConnectionState::DISCONNECTED);
}

Connection::~Connection()
{
}

void Connection::connect()
{
    connectionLock.lock();
    if (getState() == ConnectionState::DISCONNECTED || getState() == ConnectionState::ERROR)
    {
        setState(ConnectionState::CONNECTING);
        try
        {
            tcp::resolver resolver{ioc};
            auto const results = resolver.resolve(host, service);
            asio::connect(getSocket(), results.begin(), results.end());
            afterSocketConnected();
            // TODO Connection timeout??
            setState(ConnectionState::CONNECTED);
        }
        catch (boost::system::system_error const &ex)
        {
            // TODO Log, and maybe rethrow?
            setState(ConnectionState::ERROR);
        }
    }
    connectionLock.unlock();
}

void Connection::disconnect()
{
    connectionLock.lock();
    if (getState() == ConnectionState::CONNECTED)
    {
        setState(ConnectionState::DISCONNECTING);
        boost::system::error_code ec;
        getSocket().shutdown(tcp::socket::shutdown_both, ec);
        if (ec && ec != boost::system::errc::not_connected && ec != boost::asio::error::eof)
        {
            // TODO Log
            setState(ConnectionState::ERROR);
        }
        else
        {
            setState(ConnectionState::DISCONNECTED);
        }
    }
    connectionLock.unlock();
}

void Connection::setState(const ConnectionState &newState)
{
    state.store(newState);
    // TODO Fire events
}

InsecureConnection::InsecureConnection(const std::string &host, const std::string &service, const uint16_t &connectionTimeoutMs) : Connection(host, service, connectionTimeoutMs)
{
}

InsecureConnection::~InsecureConnection()
{
}

tcp::socket &InsecureConnection::getSocket()
{
    return socket;
}

SecureConnection::SecureConnection(const std::string &host, const std::string &service, const uint16_t &connectionTimeoutMs) : Connection(host, service, connectionTimeoutMs)
{
    sslCtx.set_options(ssl::context::default_workarounds | ssl::context::no_sslv2 | ssl::context::no_sslv3 | ssl::context::no_tlsv1_1 | ssl::context::no_tlsv1_1 | ssl::context::single_dh_use);
    sslCtx.load_verify_file(SSL_ROOT_CERTIFICATES_FILE);
}

SecureConnection::~SecureConnection()
{
}

tcp::socket &SecureConnection::getSocket()
{
    return stream.next_layer();
}

void SecureConnection::afterSocketConnected()
{
    stream.handshake(ssl::stream_base::client);
}