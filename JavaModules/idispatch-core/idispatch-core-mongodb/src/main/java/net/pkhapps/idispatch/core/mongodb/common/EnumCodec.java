package net.pkhapps.idispatch.core.mongodb.common;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.jetbrains.annotations.NotNull;

public class EnumCodec<T extends Enum<T>> implements Codec<T> {

    private final Class<T> enumClass;

    public EnumCodec(@NotNull Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return Enum.valueOf(enumClass, bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, T t, EncoderContext encoderContext) {
        bsonWriter.writeString(t.name());
    }

    @Override
    public Class<T> getEncoderClass() {
        return enumClass;
    }
}
