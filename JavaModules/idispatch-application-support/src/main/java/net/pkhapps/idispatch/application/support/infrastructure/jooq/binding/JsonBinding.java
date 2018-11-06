package net.pkhapps.idispatch.application.support.infrastructure.jooq.binding;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

import javax.annotation.concurrent.Immutable;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Objects;

/**
 * A JSON binding for PostgreSQL's {@code json} column type, mapping to a GSON {@link JsonElement}. The code has been
 * mostly copied from the
 * <a href="https://www.jooq.org/doc/latest/manual/code-generation/custom-data-type-bindings/">JOOQ manual</a>.
 */
@Immutable
public class JsonBinding implements Binding<Object, JsonElement> {

    @Override
    public Converter<Object, JsonElement> converter() {
        return new Converter<>() {
            @Override
            public JsonElement from(Object dbObject) {
                return dbObject == null ? JsonNull.INSTANCE : new Gson().fromJson(String.valueOf(dbObject), JsonElement.class);
            }

            @Override
            public Object to(JsonElement userObject) {
                return userObject == null || userObject == JsonNull.INSTANCE ? null : new Gson().toJson(userObject);
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @Override
            public Class<JsonElement> toType() {
                return JsonElement.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<JsonElement> ctx) throws SQLException {
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value())).sql("::json");
        } else {
            ctx.render().sql("?::json");
        }
    }

    @Override
    public void register(BindingRegisterContext<JsonElement> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<JsonElement> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null));
    }

    @Override
    public void set(BindingSetSQLOutputContext<JsonElement> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(BindingGetResultSetContext<JsonElement> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<JsonElement> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<JsonElement> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
