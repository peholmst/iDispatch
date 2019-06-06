#ifndef GISDATA_H
#define GISDATA_H

#include <QGeoCoordinate>
#include <QGeoPath>

#include "idispatchgisdata_global.h"

typedef QString MunicipalityCode;

typedef QString Name;

struct IDISPATCHGISDATASHARED_EXPORT Municipality
{
    MunicipalityCode code;
    Name nameSwe;
    Name nameFin;

    bool operator==(const Municipality& other) const
    {
        return code == other.code
                && nameSwe == other.nameSwe
                && nameFin == other.nameFin;
    }
};

class IDISPATCHGISDATASHARED_EXPORT MunicipalityBuilder
{
public:
    MunicipalityBuilder() {
    }

    MunicipalityBuilder(const Municipality original) : m(original)
    {
    }

    MunicipalityBuilder& withCode(const MunicipalityCode& code)
    {
        m.code = code;
        return *this;
    }

    MunicipalityBuilder& withNameSwe(const Name& nameSwe)
    {
        m.nameSwe = nameSwe;
        return *this;
    }

    MunicipalityBuilder& withNameFin(const Name& nameFin)
    {
        m.nameFin = nameFin;
        return *this;
    }

    Municipality build()
    {
        return m;
    }

private:
    Municipality m;
};

typedef QString GID;

struct IDISPATCHGISDATASHARED_EXPORT RoadSegment
{
    GID id;
    QGeoPath location;
    int minAddressNumberLeft;
    int maxAddressNumberLeft;
    int minAddressNumberRight;
    int maxAddressNumberRight;
    Name nameSwe;
    Name nameFin;
    MunicipalityCode municipality;
};

struct IDISPATCHGISDATASHARED_EXPORT AddressPoint
{
    GID id;
    QGeoCoordinate location;
    int number;
    Name nameSwe;
    Name nameFin;
    MunicipalityCode municipality;
};

#endif // GISDATA_H
