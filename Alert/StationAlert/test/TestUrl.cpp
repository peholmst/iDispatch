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

#include <cppunit/extensions/HelperMacros.h>

#include "Url.hpp"

using namespace idispatch::net;

class TestUrl : public CppUnit::TestFixture
{
    CPPUNIT_TEST_SUITE(TestUrl);
    CPPUNIT_TEST(test_construct_fromString_noHost);
    CPPUNIT_TEST(test_construct_fromString_domainNameWithNoPort);
    CPPUNIT_TEST(test_construct_fromString_domainNameWithPort);
    CPPUNIT_TEST_SUITE_END();

public:
    void test_construct_fromString_noHost()
    {
        Url url("file:/this/is/my/path");
        CPPUNIT_ASSERT("file" == url.getScheme());
        CPPUNIT_ASSERT("" == url.getHost());
        CPPUNIT_ASSERT(0 == url.getPort());
        CPPUNIT_ASSERT("/this/is/my/path" == url.getPath());
        CPPUNIT_ASSERT(HostType::NONE == url.getHostType());
    }

    void test_construct_fromString_domainNameWithNoPort()
    {
        Url url("https://www.foo.com/this/is/my/path");
        CPPUNIT_ASSERT("https" == url.getScheme());
        CPPUNIT_ASSERT("www.foo.com" == url.getHost());
        CPPUNIT_ASSERT(0 == url.getPort());
        CPPUNIT_ASSERT("/this/is/my/path" == url.getPath());
        CPPUNIT_ASSERT(HostType::NAME == url.getHostType());
    }

    void test_construct_fromString_domainNameWithPort()
    {
        Url url("https://www.foo.com:8443/this/is/my/path");
        CPPUNIT_ASSERT("https" == url.getScheme());
        CPPUNIT_ASSERT("www.foo.com" == url.getHost());
        CPPUNIT_ASSERT(8443 == url.getPort());
        CPPUNIT_ASSERT("/this/is/my/path" == url.getPath());
        CPPUNIT_ASSERT(HostType::NAME == url.getHostType());
    }
};

CPPUNIT_TEST_SUITE_REGISTRATION(TestUrl);