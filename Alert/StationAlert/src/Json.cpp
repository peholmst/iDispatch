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

#include "Json.hpp"
#include <sstream>
#include <cctype>

using namespace idispatch::json;

const JsonString &JsonValue::asString() const
{
    throw IllegalJsonTypeException("This value is not a string");
}

const JsonNumber &JsonValue::asNumber() const
{
    throw IllegalJsonTypeException("This value is not a number");
}

const JsonObject &JsonValue::asObject() const
{
    throw IllegalJsonTypeException("This value is not an object");
}

const JsonArray &JsonValue::asArray() const
{
    throw IllegalJsonTypeException("This value is not an array");
}

JsonObjectBuilder JsonObject::create()
{
    return JsonObjectBuilder();
}

bool JsonParser::isWhitespace(const char &chr) const
{
    return chr == ' ' || chr == '\n' || chr == '\r' || chr == '\t';
}

bool JsonParser::isNumber(const char &chr) const
{
    return isdigit(chr);
}

std::unique_ptr<JsonString> JsonParser::parseString(const std::string &jsonString, std::string::const_iterator &iterator)
{
    std::stringstream str;
    if (*iterator != '"')
    {
        throw new JsonParserException("String did not start with \"");
    }
    iterator++;
    while (iterator != jsonString.end())
    {
        if (*iterator == '"')
        {
            iterator++;
            return std::make_unique<JsonString>(JsonString(str.str()));
        }
        else if (*iterator == '\\')
        {
            iterator++;
            if (iterator != jsonString.end() && *iterator == '"')
            {
                str << '"';
                iterator++;
            }
            else
            {
                str << '\\';
            }
        }
        else
        {
            str << *iterator++;
        }
    }
    throw new JsonParserException("String did not end with \"");
}

std::unique_ptr<JsonNumber> JsonParser::parseNumber(const std::string &jsonString, std::string::const_iterator &iterator)
{
    bool decimal = false;
    bool exponent = false;
    std::stringstream str;
    while (iterator != jsonString.end())
    {
        if (*iterator == '-')
        {
            if (str.rdbuf()->in_avail() == 0)
            {
                str << *iterator++;
            }
            else
            {
                throw JsonParserException("Found - sign in the wrong place");
            }
        }
        else if (*iterator == '.')
        {
            if (!decimal)
            {
                decimal = true;
                str << *iterator++;
            }
            else
            {
                throw JsonParserException("Found . in the wrong place");
            }
        }
        else if (*iterator == 'e' || *iterator == 'E')
        {
            if (!exponent)
            {
                exponent = true;
                decimal = true;
                str << 'e';
                iterator++;
                if (*iterator == '+' || *iterator == '-')
                {
                    str << *iterator++;
                }
            }
            else
            {
                throw JsonParserException("Found exponent in the wrong place");
            }
        }
        else if (isNumber(*iterator))
        {
            str << *iterator++;
        }
        else if (isWhitespace(*iterator) || *iterator == ',' || *iterator == ']' || *iterator == '}')
        {
            break;
        }
        else
        {
            throw JsonParserException("Invalid number: " + str.str() + *iterator);
        }
    }
    if (decimal || exponent)
    {
        return std::make_unique<JsonNumber>(JsonNumber(atof(str.str().c_str())));
    }
    else
    {
        // TODO Check length of string to determinte whether to use 16, 32 or 64 bit integer.
        // Currently defaulting to 64 bit.
        return std::make_unique<JsonNumber>(JsonNumber(atoll(str.str().c_str())));
    }
}

std::unique_ptr<JsonNull> JsonParser::parseNull(const std::string &jsonString, std::string::const_iterator &iterator)
{
    if (*iterator == 'n' && *++iterator == 'u' && *++iterator == 'l' && *++iterator == 'l')
    {
        iterator++;
        if (iterator == jsonString.end() || isWhitespace(*iterator) || *iterator == ',' || *iterator == ']' || *iterator == '}')
        {
            return std::make_unique<JsonNull>(JsonNull());
        }
    }
    throw JsonParserException("Invalid token");
}

std::unique_ptr<JsonObject> JsonParser::parseObject(const std::string &jsonString, std::string::const_iterator &iterator)
{
    // TODO Implement me!
    return std::make_unique<JsonObject>(JsonObject());
}

std::unique_ptr<JsonValue> JsonParser::parse(const std::string &jsonString)
{
    for (auto iterator = jsonString.begin(); iterator != jsonString.end(); iterator++)
    {
        if (*iterator == '"')
        {
            return parseString(jsonString, iterator);
        }
        else if (isNumber(*iterator) || *iterator == '.' || *iterator == '-')
        {
            return parseNumber(jsonString, iterator);
        }
        else if (*iterator == 'n')
        {
            return parseNull(jsonString, iterator);
        }
        else if (*iterator == '{')
        {
            return parseObject(jsonString, iterator);
        }
    }
    throw JsonParserException("Illegal JSON string");
}
