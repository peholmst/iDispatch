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

#ifndef __ALERT_HPP__
#define __ALERT_HPP__

#include <chrono>
#include <string>
#include <vector>

#include "MultilingualString.hpp"

namespace alert
{
    typedef std::chrono::time_point<std::chrono::system_clock> Instant;
    typedef std::string AlertId;
    typedef std::string IncidentIdentifier;
    typedef std::string IncidentTypeCode;
    typedef std::string IncidentUrgencyCode;
    typedef std::string ResourceIdentifier;

    typedef struct
    {
        const utils::MultilingualString name;
    } Municipality;

    typedef struct
    {
        const double lat;
        const double lon;
    } GeoPoint;

    typedef struct
    {
        const utils::MultilingualString roadOrAddressPointName;
        const std::string number;
        const utils::MultilingualString intersectingRoadName;
        const std::string comments;
    } Address;

    struct Alert
    {
        AlertId alertId;
        Instant alertInstant;

        IncidentIdentifier incidentIdentifier;
        Instant incidentInstant;
        IncidentTypeCode incidentType;
        IncidentUrgencyCode incidentUrgency;

        Municipality municipality;
        GeoPoint coordinates;
        Address address;
        std::string details;

        std::vector<ResourceIdentifier> assignedResources;
        std::vector<ResourceIdentifier> resourcesToAlert;
    };
}
#endif