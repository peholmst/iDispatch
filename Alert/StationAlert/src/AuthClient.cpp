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

#include <boost/beast/http.hpp>

#include "AuthClient.hpp"
#include "Connection.hpp"

using namespace idispatch::auth;
using namespace idispatch::net;

namespace http = boost::beast::http;

AccessToken &AuthClient::getToken()
{
    auto url = authProvider.getNextUrl();
    if (url.getScheme() == "https")
    {
        SecureConnection connection(url.getHost(), url.getScheme(), 3000);
        connection.connect();
        
        connection.disconnect();
    }
    return token;
}