package net.pkhapps.idispatch.alerter.server.domain.alert.hibernate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * Hibernate user type for {@link JsonNode}.
 * <p>
 * Derived from https://github.com/thjanssen/HibernateJSONBSupport/blob/master/PostgresJSONB/src/main/java/org/thoughts/on/java/model/MyJsonType.java
 */
public class JsonType implements UserType {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class returnedClass() {
        return JsonNode.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hash(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        final var rawJson = rs.getString(names[0]);
        if (rawJson == null) {
            return null;
        }
        try {
            return objectMapper.readTree(rawJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new HibernateException("Failed to convert String to JsonNode", ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            try {
                st.setObject(index, objectMapper.writeValueAsString(value), Types.OTHER);
            } catch (Exception ex) {
                throw new HibernateException("Failed to convert JsonNode to String", ex);
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return ((JsonNode) value).deepCopy();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) this.deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
