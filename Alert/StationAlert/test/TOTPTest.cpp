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

#define BOOST_TEST_MODULE TOTP Test
#include <boost/test/included/unit_test.hpp>

#include "TOTP.hpp"

using namespace idispatch::security;

// Test data coming from https://datatracker.ietf.org/doc/html/rfc6238

const SharedSecret testSecret = "31323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334";

BOOST_AUTO_TEST_CASE(rfc6238_test_vectors)
{
    BOOST_TEST("90693936" == generateOneTimePassword(testSecret, 59).truncate(8));
    BOOST_TEST("25091201" == generateOneTimePassword(testSecret, 1111111109).truncate(8));
    BOOST_TEST("99943326" == generateOneTimePassword(testSecret, 1111111111).truncate(8));
    BOOST_TEST("93441116" == generateOneTimePassword(testSecret, 1234567890).truncate(8));
    BOOST_TEST("38618901" == generateOneTimePassword(testSecret, 2000000000).truncate(8));
    BOOST_TEST("47863826" == generateOneTimePassword(testSecret, 20000000000).truncate(8));
}

BOOST_AUTO_TEST_CASE(equality)
{
    auto otp = generateOneTimePassword(testSecret, 59);
    auto otp2 = generateOneTimePassword(testSecret, 1111111109);
    auto otp3 = generateOneTimePassword(testSecret, 59);

    BOOST_TEST((otp == otp));
    BOOST_TEST((otp != otp2));
    BOOST_TEST((otp == otp3));
}

BOOST_AUTO_TEST_CASE(copy_constructor_and_equality)
{
    auto otp = generateOneTimePassword(testSecret, 59);
    auto otp2(otp);
    BOOST_TEST((otp == otp2));
    BOOST_TEST(otp.length() == otp2.length());
    BOOST_TEST((&otp != &otp2));
}

BOOST_AUTO_TEST_CASE(copy_assignment_and_equality)
{
    auto otp = generateOneTimePassword(testSecret, 59);
    OneTimePassword otp2;

    BOOST_TEST((otp != otp2));
    BOOST_TEST(otp.length() != otp2.length());

    otp2 = otp;

    BOOST_TEST((otp == otp2));
    BOOST_TEST(otp.length() == otp2.length());
    BOOST_TEST((&otp != &otp2));
}

BOOST_AUTO_TEST_CASE(move_assignment_and_equality)
{
    OneTimePassword otp;
    otp = generateOneTimePassword(testSecret, 59);

    BOOST_TEST(otp.length() > (size_t)0);
}