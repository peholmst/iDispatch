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

#ifndef __CONNECTION_HPP__
#define __CONNECTION_HPP__

#include <atomic>
#include <boost/asio.hpp>
#include <boost/asio/ip/tcp.hpp>
#include <boost/asio/ssl.hpp>
#include <mutex>
#include <vector>

namespace asio = boost::asio;
namespace ssl = asio::ssl;
using tcp = boost::asio::ip::tcp;

namespace idispatch::net
{
    enum ConnectionState
    {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        ERROR
    };

    class Connection
    {
    public:
        Connection(const std::string &host, const std::string &service, const uint16_t &connectionTimeoutMs);
        virtual ~Connection();
        void connect();
        void disconnect();
        ConnectionState getState() const { return state.load(); }

    protected:
        asio::io_context ioc;
        virtual tcp::socket &getSocket() = 0;
        virtual void afterSocketConnected(){};

    private:
        std::string host;
        std::string service;
        uint16_t connectionTimeoutMs;
        std::atomic<ConnectionState> state;
        std::recursive_mutex connectionLock;
        void setState(const ConnectionState &newState);
    };

    class InsecureConnection : public Connection
    {
    public:
        InsecureConnection(const std::string &host, const std::string &service, const uint16_t &connectionTimeoutMs);
        virtual ~InsecureConnection();

    protected:
        virtual tcp::socket &getSocket();

    private:
        tcp::socket socket{ioc};
    };

    class SecureConnection : public Connection
    {
    public:
        SecureConnection(const std::string &host, const std::string &service, const uint16_t &connectionTimeoutMs);
        virtual ~SecureConnection();

    protected:
        virtual tcp::socket &getSocket();
        virtual void afterSocketConnected();

    private:
        ssl::context sslCtx{ssl::context::sslv23_client};
        ssl::stream<tcp::socket> stream{ioc, sslCtx};
    };
};

#endif