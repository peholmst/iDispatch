// iDispatch Station Alert
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

#ifndef __ALERT_CLIENT_HPP__
#define __ALERT_CLIENT_HPP__

#include <string>

struct AlertServerHost
{
    std::string host;
    uint16_t port;
};

struct AlertServerCredentials
{
    std::string clientId;
    std::string clientSecret;
};

class AlertClient
{
public:
    AlertClient(const AlertServerCredentials credentials, const AlertServerHost hosts[]);
    ~AlertClient();
};

#endif