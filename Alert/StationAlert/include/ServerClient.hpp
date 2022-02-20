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

#ifndef __SERVER_CLIENT_HPP__
#define __SERVER_CLIENT_HPP__

#include <boost/asio/connect.hpp>
#include <boost/asio/ip/tcp.hpp>
#include <boost/beast/core.hpp>
#include <boost/beast/websocket.hpp>

#include <string>
#include <vector>

namespace beast = boost::beast;
namespace http = beast::http;
namespace websocket = beast::websocket;
namespace net = boost::asio;
using tcp = boost::asio::ip::tcp;

namespace client
{
    struct Server
    {
        std::string hostname;
        unsigned int port;
        std::string contextPath;
        bool tls;
    };

    struct ClientParameters
    {
        signed long receiverId;
        std::string username;
        std::string password;
        std::vector<Server> servers;
    };

    enum ConnectionState
    {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING
    };

    class ServerClient
    {
    private:
        net::io_context ioc;
        tcp::resolver resolver;
        websocket::stream<tcp::socket> ws;
        ConnectionState state;
        ClientParameters parameters;

        void doSetState(const ConnectionState state) { this->state = state; };
        void doConnect(const Server &server);

    public:
        ServerClient(const ClientParameters parameters);
        ~ServerClient();
        void connect();
        void disconnect();
        ConnectionState getState() const { return state; }
    };
};

#endif