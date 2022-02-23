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

    inline u_char hexCharToByte(const char &chr)
    {
        if (chr >= 48 && chr <= 57) // 0-9
        {
            return chr - 48;
        }
        else if (chr >= 65 && chr <= 70) // A-B
        {
            return chr - 55;
        }
        else if (chr >= 97 && chr <= 102) // a-b
        {
            return chr - 87;
        }
        else
        {
            throw std::invalid_argument("The shared secret is not a valid hex string");
        }
    }

    size_t sharedSecretToBytes(const SharedSecret &sharedSecret, u_char buffer[], const size_t bufferSize)
    {
        auto bytes = (sharedSecret.length() + 1) / 2; // Force the number of bytes to be even
        if (bufferSize < bytes)
        {
            throw std::overflow_error("The buffer is too small (should be at least " + std::to_string(bytes) + " bytes)");
        }
        size_t bufIndex = 0;
        for (size_t i = 0; i < sharedSecret.length(); i += 2)
        {
            buffer[bufIndex] = hexCharToByte(sharedSecret.at(i)) << 4;
            if (i < sharedSecret.length() - 1)
            {
                buffer[bufIndex] += hexCharToByte(sharedSecret.at(i + 1));
            }
            ++bufIndex;
        }

        return bytes;
    }

    OneTimePassword generateOneTimePassword(const SharedSecret &sharedSecret, const std::time_t &now)
    {
        const uint64_t T = (((uint64_t)now) - T0) / X;
        const size_t keyLength = std::max((sharedSecret.length() + 1) / 2, (size_t)64);
        unsigned char key[keyLength] = {};
        sharedSecretToBytes(sharedSecret, key, sizeof(key));
        unsigned char data[8];
        data[0] = (T >> 56) & 0xFF;
        data[1] = (T >> 48) & 0xFF;
        data[2] = (T >> 40) & 0xFF;
        data[3] = (T >> 32) & 0xFF;
        data[4] = (T >> 24) & 0xFF;
        data[5] = (T >> 16) & 0xFF;
        data[6] = (T >> 8) & 0xFF;
        data[7] = T & 0xFF;

        unsigned char hash[64];
        unsigned int hashLength;

        if (HMAC(EVP_sha512(), key, keyLength, data, sizeof(data), hash, &hashLength) == nullptr)
        {
            throw std::runtime_error("An error occurred while generating the OTP");
        }

        return std::move(OneTimePassword(hash, hashLength));
    }
};