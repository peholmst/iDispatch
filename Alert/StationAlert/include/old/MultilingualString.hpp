// iDispatch Station Alert
// Copyright (C) 2021 Petter Holmström
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

#ifndef __MULTILINGUAL_STRING_HPP__
#define __MULTILINGUAL_STRING_HPP__

#include <initializer_list>
#include <map>
#include <stdexcept>
#include <string>

#include "Locale.hpp"

namespace utils
{
    class MultilingualString
    {
    public:
        static MultilingualString fromMonolingualString(const std::string &string)
        {
            return MultilingualString{{ROOT_LOCALE, string}};
        }

        static MultilingualString fromBilingualString(const Locale &l1, std::string &s1, const Locale &l2, std::string &s2)
        {
            return MultilingualString({{l1, s2}, {l2, s2}, {ROOT_LOCALE, s1}});
        }

        MultilingualString(std::initializer_list<std::pair<const Locale, const std::string>> values) : values(values)
        {
            if (this->values.find(ROOT_LOCALE) == this->values.end())
            {
                throw std::invalid_argument("No default value found in map");
            }
        }

        const std::string localizedValue(const Locale &locale) const
        {
            try
            {
                return values.at(locale);
            }
            catch (std::out_of_range ex)
            {
                return defaultValue();
            }
        }
        const std::string defaultValue() const
        {
            return values.at(ROOT_LOCALE);
        }

    private:
        std::map<Locale, const std::string> values;
    };

    const MultilingualString EMPTY_MULTILINGUAL_STRING{{ROOT_LOCALE, ""}};
};

#endif
