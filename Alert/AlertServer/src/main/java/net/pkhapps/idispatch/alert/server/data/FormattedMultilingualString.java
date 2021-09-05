// iDispatch Alert Server
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

package net.pkhapps.idispatch.alert.server.data;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * An implementation of {@link MultilingualString} that builds formatted strings using a {@link MessageFormat} with
 * support for other {@link MultilingualString}s as arguments. The locales will be taken into account when producing the
 * final string.
 */
public class FormattedMultilingualString implements MultilingualString {

    private final MessageFormat messageFormat;
    private final List<Object> arguments;

    private FormattedMultilingualString(String pattern, Object... arguments) {
        messageFormat = new MessageFormat(pattern);
        this.arguments = List.of(arguments);
    }

    /**
     * Creates a new {@code FormattedMultilingualString} with the given pattern and arguments.
     *
     * @param pattern   the {@link MessageFormat} pattern to use, must not be {@code null}.
     * @param arguments the arguments to use when constructing the formatted strings. Can include other {@link
     *                  MultilingualString}s.
     * @return a new {@code FormattedMultilingualString}.
     */
    public static FormattedMultilingualString format(String pattern, Object... arguments) {
        return new FormattedMultilingualString(pattern, arguments);
    }

    @Override
    public String localizedValue(Locale locale) {
        return messageFormat.format(prepareArguments(s -> s.localizedValue(locale)));
    }

    @Override
    public String defaultValue() {
        return messageFormat.format(prepareArguments(MultilingualString::defaultValue));
    }

    private Object[] prepareArguments(Function<MultilingualString, String> stringValueFunction) {
        var argArray = new Object[arguments.size()];
        for (int i = 0; i < arguments.size(); ++i) {
            var arg = arguments.get(i);
            if (arg instanceof MultilingualString) {
                argArray[i] = stringValueFunction.apply((MultilingualString) arg);
            } else {
                argArray[i] = arg;
            }
        }
        return argArray;
    }
}
