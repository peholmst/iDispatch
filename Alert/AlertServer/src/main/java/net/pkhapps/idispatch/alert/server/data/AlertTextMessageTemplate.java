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

import static java.util.Objects.requireNonNull;

/**
 * An entity representing an alert text message template that together with {@link
 * net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter} can be used to convert {@link Alert}s
 * into text.
 */
public class AlertTextMessageTemplate extends Entity<AlertTextMessageTemplateId> {

    private final String name;
    private final String contentType;
    private final String template;

    /**
     * Creates a new {@code AlertTextMessageTemplate}.
     *
     * @param id          the ID of the template, must not be {@code null}.
     * @param name        the name of the template, must not be {@code null}.
     * @param contentType the content type of the template, must not be {@code null}.
     * @param template    the template string, must not be {@code null}.
     */
    public AlertTextMessageTemplate(AlertTextMessageTemplateId id, String name, String contentType, String template) {
        super(id);
        this.name = requireNonNull(name, "name must not be null");
        this.contentType = requireNonNull(contentType, "contentType must not be null");
        this.template = requireNonNull(template, "template must not be null");
    }

    /**
     * The name of this template. This is mainly used to help human users identify the template.
     */
    public String name() {
        return name;
    }

    /**
     * The content type of this template. For example, a template that generates a JSON string would have a content type
     * of {@code application/json}.
     */
    public String contentType() {
        return contentType;
    }

    /**
     * The template string that will be passed to {@link net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter#formatAlertMessage(Alert,
     * String)}.
     */
    public String template() {
        return template;
    }
}
