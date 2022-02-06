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

#include "Url.hpp"

#include <regex>
#include <sstream>

using namespace idispatch::net;

const std::regex IPV4_REGEX("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
const std::regex IPV6_REGEX("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");

bool isIpv4(const std::string &host)
{
    return std::regex_match(host, IPV4_REGEX);
}

bool isIpv6(const std::string &host)
{
    return std::regex_match(host, IPV6_REGEX);
}

void Url::parse(const std::string &url)
{
    std::stringstream component;
    std::basic_string<char>::size_type position = 0;

    // Initialize port to 0
    port = 0;

    // Scheme
    while (position < url.size() && url[position] != ':')
    {
        component << url[position++];
    }
    scheme = component.str();
    component.clear();
    component.str(std::string());
    position++;

    // Host
    if (position < url.size() - 1 && url[position] == '/' && url[position + 1] == '/')
    {
        position += 2;
        while (position < url.size() && url[position] != ':' && url[position] != '/')
        {
            component << url[position++];
        }
        host = component.str();
        component.clear();
        component.str(std::string());

        // Port
        if (position < url.size() && url[position] == ':')
        {
            position++;
            while (position < url.size() && url[position] != '/')
            {
                component << url[position++];
            }
            try
            {
                port = std::stoi(component.str());
                component.clear();
                component.str(std::string());
            }
            catch (std::invalid_argument const &ex)
            {
                throw MalformedUrlException("Invalid port: " + component.str());
            }
        }
    }

    // Path
    while (position < url.size() && url[position] != '?')
    {
        component << url[position++];
    }
    path = component.str();

    // TODO Validate everything!

    if (host.empty())
    {
        hostType = NONE;
    }
    else if (isIpv4(host))
    {
        hostType = IPV4;
    }
    else if (isIpv6(host))
    {
        hostType = IPV6;
    }
    else
    {
        hostType = NAME;
    }
}