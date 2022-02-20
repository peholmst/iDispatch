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

#ifndef __AUTH_CLIENT_HPP__
#define __AUTH_CLIENT_HPP__

#include "Url.hpp"

namespace idispatch::auth
{
    struct Credentials
    {
        std::string clientId;
        std::string clientSecret;
    };

    class AuthProvider
    {
    public:
        AuthProvider(const idispatch::net::Url &url) : url(url) {}
        idispatch::net::Url &getNextUrl() { return url; }

    private:
        idispatch::net::Url url;
    };

    class AccessToken
    {
    };

    class AuthClient
    {
    public:
        AuthClient(const Credentials &credentials, const AuthProvider &authProvider) : credentials(credentials), authProvider(authProvider){};
        AccessToken &getToken();

    private:
        Credentials credentials;
        AuthProvider authProvider;
        AccessToken token;
    };
}

#endif
