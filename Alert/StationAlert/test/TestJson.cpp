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

#include "Json.hpp"

using namespace idispatch::json;

class TestJson : public CppUnit::TestFixture
{
    CPPUNIT_TEST_SUITE(TestJson);
    CPPUNIT_TEST(test_JsonString);
    CPPUNIT_TEST(test_JsonNumber_int16);
    CPPUNIT_TEST(test_JsonNumber_int32);
    CPPUNIT_TEST(test_JsonNumber_int64);
    CPPUNIT_TEST(test_JsonNumber_double);
    CPPUNIT_TEST(test_JsonObject);
    CPPUNIT_TEST(test_parse_string);
    CPPUNIT_TEST(test_parse_integer);
    CPPUNIT_TEST(test_parse_double);
    CPPUNIT_TEST(test_parse_fraction);
    CPPUNIT_TEST(test_parse_negative_double);
    CPPUNIT_TEST(test_parse_negative_integer);
    CPPUNIT_TEST(test_parse_exponent);
    CPPUNIT_TEST(test_parse_null);
    CPPUNIT_TEST(test_parse_emptyObject);
    CPPUNIT_TEST_SUITE_END();

public:
    void test_JsonString()
    {
        JsonString s1("hello");
        JsonString s2("world");
        JsonString s3(s1);
        CPPUNIT_ASSERT(s1.isString());
        CPPUNIT_ASSERT(&s1 == &s1.asString());
        CPPUNIT_ASSERT("hello" == s1.str());
        CPPUNIT_ASSERT(s1 != s2);
        CPPUNIT_ASSERT(s1 == s3);
    }

    void test_JsonNumber_int16()
    {
        JsonNumber n1((int16_t)123);
        JsonNumber n2((int16_t)456);
        JsonNumber n3(n1);
        CPPUNIT_ASSERT(n1.isNumber());
        CPPUNIT_ASSERT(&n1 == &n1.asNumber());
        CPPUNIT_ASSERT(n1.isInt16());
        CPPUNIT_ASSERT(123 == n1.int16());
        CPPUNIT_ASSERT((int32_t)123 == n1.int32());
        CPPUNIT_ASSERT((int64_t)123 == n1.int64());
        CPPUNIT_ASSERT(123.0 == n1.dbl());
        CPPUNIT_ASSERT(n1 != n2);
        CPPUNIT_ASSERT(n1 == n3);
    }

    void test_JsonNumber_int32()
    {
        JsonNumber n((int32_t)32768);
        CPPUNIT_ASSERT(n.isInt32());
        CPPUNIT_ASSERT((int32_t)32768 == n.int32());
        CPPUNIT_ASSERT((int64_t)32768 == n.int64());
        CPPUNIT_ASSERT(32768.0 == n.dbl());
    }

    void test_JsonNumber_int64()
    {
        JsonNumber n((int64_t)2147483648);
        CPPUNIT_ASSERT(n.isInt64());
        CPPUNIT_ASSERT((int64_t)2147483648 == n.int64());
        CPPUNIT_ASSERT(2147483648.0 == n.dbl());
    }

    void test_JsonNumber_double()
    {
        JsonNumber n((double)123.4);
        CPPUNIT_ASSERT(n.isDouble());
        CPPUNIT_ASSERT(123 == n.int16());
        CPPUNIT_ASSERT((int32_t)123 == n.int32());
        CPPUNIT_ASSERT((int64_t)123 == n.int64());
        CPPUNIT_ASSERT(123.4 == n.dbl());
    }

    void test_JsonObject()
    {
        JsonString attr_str("hello");
        JsonNumber attr_nmbr(123);

        JsonObjectBuilder builder = JsonObject::create();
        builder
            .with("string", attr_str)
            .with("string2", JsonString("world"))
            .with("number", attr_nmbr)
            .with("number2", JsonNumber(456));

        JsonObject o = builder;
        CPPUNIT_ASSERT(o.isObject());
        CPPUNIT_ASSERT(o.contains("string"));
        CPPUNIT_ASSERT(o.contains("number"));
        CPPUNIT_ASSERT(attr_str == o["string"]);
        CPPUNIT_ASSERT(attr_nmbr == o["number"]);
        CPPUNIT_ASSERT("world" == o["string2"].asString().str());
        CPPUNIT_ASSERT(456 == o["number2"].asNumber().int16());
        CPPUNIT_ASSERT(o == o.asObject());
        CPPUNIT_ASSERT(o == builder);

        builder.with("number3", JsonNumber(789));
        JsonObject o2 = builder;
        CPPUNIT_ASSERT(o2.contains("string"));
        CPPUNIT_ASSERT(o2.contains("string2"));
        CPPUNIT_ASSERT(o2.contains("number"));
        CPPUNIT_ASSERT(o2.contains("number2"));
        CPPUNIT_ASSERT(o2.contains("number3"));
        CPPUNIT_ASSERT(o != o2);
    }

    void test_parse_string()
    {
        auto ptr = JsonParser::create().parse("  \"hello \\\"world\\\"\"  ");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isString());
        CPPUNIT_ASSERT("hello \"world\"" == obj.asString().str());
    }

    void test_parse_integer()
    {
        auto ptr = JsonParser::create().parse("  12345 ");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNumber());
        CPPUNIT_ASSERT(12345 == obj.asNumber().int16());
    }

    void test_parse_double()
    {
        auto ptr = JsonParser::create().parse("1.5");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNumber());
        CPPUNIT_ASSERT(1.5 == obj.asNumber().dbl());
    }

    void test_parse_fraction()
    {
        auto ptr = JsonParser::create().parse(".5");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNumber());
        CPPUNIT_ASSERT(.5 == obj.asNumber().dbl());
    }

    void test_parse_negative_double()
    {
        auto ptr = JsonParser::create().parse("-345.6");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNumber());
        CPPUNIT_ASSERT(-345.6 == obj.asNumber().dbl());
    }

    void test_parse_negative_integer()
    {
        auto ptr = JsonParser::create().parse("-12345");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNumber());
        CPPUNIT_ASSERT(-12345 == obj.asNumber().int16());
    }

    void test_parse_exponent()
    {
        auto ptr = JsonParser::create().parse("1.0e+2");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNumber());
        CPPUNIT_ASSERT(1.0e2 == obj.asNumber().dbl());
    }

    void test_parse_null()
    {
        auto ptr = JsonParser::create().parse("null ");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isNull());
    }

    void test_parse_emptyObject()
    {
        auto ptr = JsonParser::create().parse("{}");
        JsonValue &obj = *ptr;
        CPPUNIT_ASSERT(obj.isObject());
        CPPUNIT_ASSERT(0 == obj.asObject().size());
    }
};

CPPUNIT_TEST_SUITE_REGISTRATION(TestJson);
