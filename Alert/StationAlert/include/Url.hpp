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

#ifndef __URL_HPP__
#define __URL_HPP__

#include <stdexcept>
#include <string>

namespace idispatch::net
{
    enum HostType
    {
        NONE,
        IPV4,
        IPV6,
        NAME
    };

    class MalformedUrlException : public std::invalid_argument
    {
    public:
        MalformedUrlException(const std::string &reason) : std::invalid_argument(reason) {}
    };

    // Please note: This class only contains the minimum features required by Station Alert. It is not meant to be a general purpose
    // URL type for all kinds of use cases.

    class Url
    {
    public:
        Url(const std::string &url) { parse(url); }

        const std::string &getScheme() const { return scheme; }
        const std::string &getHost() const { return host; }
        const std::string &getPath() const { return path; }
        const uint16_t &getPort() const { return port; }
        const HostType &getHostType() const { return hostType; }

    private:
        void parse(const std::string &url);

        std::string scheme;
        std::string host;
        HostType hostType;
        std::string path;
        uint16_t port;
    };
};

#endif