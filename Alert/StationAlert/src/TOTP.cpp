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

#include "TOTP.hpp"

#include <openssl/hmac.h>

namespace idispatch::security
{
    constexpr uint16_t T0 = 0;
    constexpr uint16_t X = 30;

    OneTimePassword generateOneTimePassword(const SharedSecret &sharedSecret, const std::time_t &now)
    {
        const uint64_t T = (((uint64_t)now) - T0) / X;
        u_char key[64] = {};
        sharedSecret.copyBytes(key, sizeof(key));
        u_char data[8];
        data[0] = (T >> 56) & 0xFF;
        data[1] = (T >> 48) & 0xFF;
        data[2] = (T >> 40) & 0xFF;
        data[3] = (T >> 32) & 0xFF;
        data[4] = (T >> 24) & 0xFF;
        data[5] = (T >> 16) & 0xFF;
        data[6] = (T >> 8) & 0xFF;
        data[7] = T & 0xFF;

        u_char hash[64];
        uint32_t hashLength;

        if (HMAC(EVP_sha512(), key, sizeof(key), data, sizeof(data), hash, &hashLength) == nullptr)
        {
            throw std::runtime_error("An error occurred while generating the OTP");
        }

        return std::move(OneTimePassword(hash, hashLength));
    }
};