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

#ifndef __TOTP_HPP__
#define __TOTP_HPP__

#include <algorithm>
#include <ctime>
#include <exception>
#include <iterator>
#include <string>
#include <vector>

namespace idispatch::security
{
    class OneTimePassword
    {
    private:
        u_char *otp;
        size_t otpLength;
        static constexpr uint32_t digitsPower[] = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public:
        OneTimePassword() : otp(nullptr), otpLength(0) {}

        OneTimePassword(const u_char otp[], const size_t &otpLength)
            : otp(otpLength == 0 ? nullptr : new u_char[otpLength]),
              otpLength(otpLength)
        {
            std::copy(otp, otp + otpLength, this->otp);
        }

        OneTimePassword(const OneTimePassword &original) : OneTimePassword(original.otp, original.otpLength) {}

        OneTimePassword(OneTimePassword &&original)
            : otp(original.otp),
              otpLength(original.otpLength)
        {
            original.otp = nullptr;
        }

        ~OneTimePassword()
        {
            if (otp != nullptr)
            {
                delete[] otp;
            }
        }

        inline size_t length() const { return otpLength; }

        std::string truncate(const size_t &digits) const
        {
            if (digits > 8)
            {
                throw std::out_of_range("Number of digits must not be greater than 8");
            }
            if (otp == nullptr)
            {
                return "";
            }
            size_t offset = otp[otpLength - 1] & 0xf;
            uint32_t binCode = (otp[offset] & 0x7f) << 24 | (otp[offset + 1] & 0xff) << 16 | (otp[offset + 2] & 0xff) << 8 | (otp[offset + 3] & 0xff);

            return std::to_string(binCode % digitsPower[digits]);
        }

        friend bool operator==(const OneTimePassword &l, const OneTimePassword &r);

        inline OneTimePassword &operator=(const OneTimePassword &other)
        {
            if (otp != nullptr)
            {
                delete[] otp;
            }
            otp = new u_char[other.otpLength];
            otpLength = other.otpLength;
            std::copy(other.otp, other.otp + otpLength, otp);
            return *this;
        }

        inline OneTimePassword &operator=(OneTimePassword &&other)
        {
            if (this != &other)
            {
                if (otp != nullptr)
                {
                    delete[] otp;
                }
                otp = other.otp;
                otpLength = other.otpLength;
                other.otp = nullptr;
            }
            return *this;
        }
    };

    inline bool operator==(const OneTimePassword &l, const OneTimePassword &r)
    {
        if (l.otp == nullptr || r.otp == nullptr)
        {
            return false;
        }
        if (l.otpLength != r.otpLength)
        {
            return false;
        }
        return std::equal(l.otp, l.otp + l.otpLength, r.otp);
    }

    inline bool operator!=(const OneTimePassword &l, const OneTimePassword &r)
    {
        return !(l == r);
    }

    class SharedSecret
    {
    private:
        u_char *secret;
        size_t secretLength;

        inline static u_char hexCharToByte(const char &chr)
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

        SharedSecret(u_char *data, const size_t &length)
            : secret(data), secretLength(length) {}

        SharedSecret(SharedSecret &&original)
            : secret(original.secret), secretLength(original.secretLength)
        {
            original.secret = nullptr;
        }

        inline void copyBytes(u_char *destination, const size_t &length) const
        {
            for (size_t i = 0; i < length; ++i)
            {
                destination[i] = secret[i % secretLength];
            }
        }

    public:
        SharedSecret(const SharedSecret &original) = delete;

        ~SharedSecret()
        {
            if (secret != nullptr)
            {
                delete[] secret;
            }
        }

        inline size_t length() const { return secretLength; }

        OneTimePassword &operator=(const OneTimePassword &other) = delete;

        static SharedSecret fromAsciiString(const std::string &str)
        {
            u_char *secret = new u_char[str.length()];
            std::copy(str.c_str(), str.c_str() + str.length(), secret);
            return std::move(SharedSecret(secret, str.length()));
        }

        static SharedSecret fromHexString(const std::string &str)
        {
            size_t length = (str.length() + 1) / 2; // Force the number of bytes to be even
            u_char *secret = new u_char[length];
            for (size_t i = 0; i < str.length(); i += 2)
            {
                u_char &byte = secret[i / 2];
                byte = hexCharToByte(str.at(i)) << 4;
                if (i < str.length() - 1)
                {
                    byte += hexCharToByte(str.at(i + 1));
                }
            }
            return std::move(SharedSecret(secret, length));
        }

        friend OneTimePassword generateOneTimePassword(const SharedSecret &sharedSecret, const std::time_t &now);
    };

    OneTimePassword generateOneTimePassword(const SharedSecret &sharedSecret, const std::time_t &now);
};

#endif