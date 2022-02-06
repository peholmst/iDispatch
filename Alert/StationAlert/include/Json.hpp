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

#ifndef __JSON_HPP__
#define __JSON_HPP__

#include <map>
#include <memory>
#include <optional>
#include <string>
#include <stdexcept>
#include <variant>

namespace idispatch::json
{
    class JsonString;
    class JsonNumber;
    class JsonObject;
    class JsonArray;

    class IllegalJsonTypeException : public std::logic_error
    {
    public:
        IllegalJsonTypeException(const std::string &reason) : std::logic_error(reason) {}
    };

    class JsonParserException : public std::invalid_argument
    {
    public:
        JsonParserException(const std::string &reason) : std::invalid_argument(reason) {}
    };

    class JsonValue
    {
    public:
        JsonValue() = default;
        virtual ~JsonValue() = default;
        virtual bool isString() const { return false; }
        virtual bool isNumber() const { return false; }
        virtual bool isObject() const { return false; }
        virtual bool isArray() const { return false; }
        virtual bool isNull() const { return false; }

        virtual const JsonString &asString() const;
        virtual const JsonNumber &asNumber() const;
        virtual const JsonObject &asObject() const;
        virtual const JsonArray &asArray() const;
    };

    class JsonString : public JsonValue
    {
    public:
        JsonString(const std::string &value) : value(value){};
        virtual bool isString() const { return true; }
        virtual const JsonString &asString() const { return *this; }
        const std::string &str() const { return value; }
        bool operator==(const JsonString &other) const { return value == other.value; }
        bool operator!=(const JsonString &other) const { return value != other.value; }

    private:
        std::string value;
    };

    class JsonNumber : public JsonValue
    {
    public:
        JsonNumber(const int16_t &value) : value(value){};
        JsonNumber(const int32_t &value) : value(value){};
        JsonNumber(const int64_t &value) : value(value){};
        JsonNumber(const double &value) : value(value){};
        virtual bool isNumber() const { return true; }
        virtual const JsonNumber &asNumber() const { return *this; }
        bool isInt16() const { return value.index() == 0; }
        bool isInt32() const { return value.index() == 1; }
        bool isInt64() const { return value.index() == 2; }
        bool isDouble() const { return value.index() == 3; }
        const int16_t int16() const
        {
            if (isInt16())
            {
                return std::get<int16_t>(value);
            }
            else if (isInt32())
            {
                return (int16_t)int32();
            }
            else if (isInt64())
            {
                return (int16_t)int64();
            }
            else
            {
                return (int16_t)dbl();
            }
        }
        const int32_t int32() const
        {
            if (isInt16())
            {
                return (int32_t)int16();
            }
            else if (isInt32())
            {
                return std::get<int32_t>(value);
            }
            else if (isInt64())
            {
                return (int32_t)int64();
            }
            else
            {
                return (int32_t)dbl();
            }
        }
        const int64_t int64() const
        {
            if (isInt16())
            {
                return (int64_t)int16();
            }
            else if (isInt32())
            {
                return (int64_t)int32();
            }
            else if (isInt64())
            {
                return std::get<int64_t>(value);
            }
            else
            {
                return (int64_t)dbl();
            }
        }
        const double dbl() const
        {
            if (isInt16())
            {
                return (double)int16();
            }
            else if (isInt32())
            {
                return (double)int32();
            }
            else if (isInt64())
            {
                return (double)int64();
            }
            else
            {
                return std::get<double>(value);
            }
        }
        bool operator==(const JsonNumber &other) const { return value == other.value; }
        bool operator!=(const JsonNumber &other) const { return value != other.value; }

    private:
        std::variant<int16_t, int32_t, int64_t, double> value;
    };

    class JsonObjectBuilder;

    class JsonObject : public JsonValue
    {
    public:
        JsonObject() : value({}) {}
        JsonObject(const std::map<std::string, std::shared_ptr<JsonValue>> &value) : value(value) {}
        virtual bool isObject() const { return true; }
        virtual const JsonObject &asObject() const { return *this; }
        bool contains(const std::string &name) const { return value.count(name) > 0; }
        std::size_t size() const { return value.size(); }
        const JsonValue &operator[](const std::string &name) const { return *value.at(name); }
        bool operator==(const JsonObject &other) const { return value == other.value; }
        bool operator!=(const JsonObject &other) const { return value != other.value; }
        static JsonObjectBuilder create();

    private:
        std::map<std::string, std::shared_ptr<JsonValue>> value;
    };

    // Don't implement until needed
    class JsonArray : public JsonValue
    {
    public:
        virtual bool isArray() const { return true; }
        virtual const JsonArray &asArray() const { return *this; }
    };

    class JsonNull : public JsonValue
    {
    public:
        virtual bool isNull() const { return true; }
        bool operator==(const JsonNull &other) const { return true; }
        bool operator!=(const JsonNull &other) const { return false; }
    };

    inline bool operator==(const JsonValue &lhs, const JsonValue &rhs)
    {
        if (lhs.isString() && rhs.isString())
        {
            return lhs.asString() == rhs.asString();
        }
        else if (lhs.isNumber() && rhs.isNumber())
        {
            return lhs.asNumber() == rhs.asNumber();
        }
        else if (lhs.isObject() && rhs.isObject())
        {
            return lhs.asObject() == rhs.asObject();
        }
        // TODO Array
        else if (lhs.isNull() && rhs.isNull())
        {
            return true;
        }
        return false;
    }

    inline bool operator!=(const JsonValue &lhs, const JsonValue &rhs)
    {
        return !(lhs == rhs);
    }

    class JsonObjectBuilder
    {
    public:
        JsonObjectBuilder &with(const std::string &name, const JsonString &value)
        {
            attributes[name] = std::make_shared<JsonString>(value);
            return *this;
        }
        JsonObjectBuilder &with(const std::string &name, const JsonNumber &value)
        {
            attributes[name] = std::make_shared<JsonNumber>(value);
            return *this;
        }
        JsonObjectBuilder &with(const std::string &name, const JsonObject &attribute);
        JsonObjectBuilder &with(const std::string &name, const JsonArray &attribute);
        JsonObjectBuilder &with(const std::string &name, const JsonNull &attribute);
        operator JsonObject() const { return std::move(JsonObject(attributes)); }

    private:
        std::map<std::string, std::shared_ptr<JsonValue>> attributes;
    };

    class JsonParser
    {
    public:
        std::unique_ptr<JsonValue> parse(const std::string &jsonString);
        static JsonParser create()
        {
            return JsonParser();
        }

    private:
        bool isWhitespace(const char &chr) const;
        bool isNumber(const char &chr) const;
        std::unique_ptr<JsonString> parseString(const std::string &jsonString, std::string::const_iterator &iterator);
        std::unique_ptr<JsonNumber> parseNumber(const std::string &jsonString, std::string::const_iterator &iterator);
        std::unique_ptr<JsonNull> parseNull(const std::string &jsonString, std::string::const_iterator &iterator);
        std::unique_ptr<JsonObject> parseObject(const std::string &jsonString, std::string::const_iterator &iterator);
    };
};

#endif