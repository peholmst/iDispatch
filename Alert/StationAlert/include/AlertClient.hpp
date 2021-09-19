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