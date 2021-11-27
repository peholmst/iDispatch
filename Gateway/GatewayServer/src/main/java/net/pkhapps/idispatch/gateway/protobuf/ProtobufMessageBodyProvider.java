/*
 * iDispatch Gateway Server
 * Copyright (C) 2021 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.gateway.protobuf;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Provides support for sending and receiving Protobuf messages. Roughly based on Spring Web's {@code
 * ProtobufHttpMessageConverter}.
 */
@Provider
@Produces(ContentTypes.PROTOBUF)
@Consumes(ContentTypes.PROTOBUF)
public class ProtobufMessageBodyProvider implements MessageBodyReader<Message>, MessageBodyWriter<Message> {

    public static final String X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema";
    public static final String X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message";

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return ContentTypes.PROTOBUF_TYPE.isCompatible(mediaType) && Message.class.isAssignableFrom(type);
    }

    @Override
    public Message readFrom(Class<Message> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                            MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        if (ContentTypes.PROTOBUF_TYPE.isCompatible(mediaType)) {
            var builder = getMessageBuilder(type);
            builder.mergeFrom(entityStream);
            return builder.build();
        } else {
            throw new WebApplicationException(Response.Status.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    private Message.Builder getMessageBuilder(Class<? extends Message> type) throws IOException {
        try {
            return (Message.Builder) type.getMethod("newBuilder").invoke(type);
        } catch (Exception ex) {
            throw new IOException("No invocable newBuilder() method on " + type, ex);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return ContentTypes.PROTOBUF_TYPE.isCompatible(mediaType) && Message.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(Message message, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        if (ContentTypes.PROTOBUF_TYPE.isCompatible(mediaType)) {
            httpHeaders.putSingle(X_PROTOBUF_SCHEMA_HEADER, message.getDescriptorForType().getFile().getName());
            httpHeaders.putSingle(X_PROTOBUF_MESSAGE_HEADER, message.getDescriptorForType().getFullName());
            var codedOutputStream = CodedOutputStream.newInstance(entityStream);
            message.writeTo(codedOutputStream);
            codedOutputStream.flush();
        } else {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
    }
}
