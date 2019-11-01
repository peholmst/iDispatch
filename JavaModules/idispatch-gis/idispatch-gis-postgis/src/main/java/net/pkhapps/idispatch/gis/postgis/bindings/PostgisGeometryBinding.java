package net.pkhapps.idispatch.gis.postgis.bindings;

import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.postgis.Geometry;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;

/**
 * TODO Document me
 */
public class PostgisGeometryBinding implements Binding<Object, Geometry> {

    @Override
    public Converter<Object, Geometry> converter() {
        return new PostgisGeometryConverter();
    }

    @Override
    public void sql(BindingSQLContext<Geometry> ctx) throws SQLException {
        if (ctx.render().paramType() == ParamType.INLINED) {
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value())).sql("::geometry");
        } else {
            ctx.render().visit(DSL.sql("?::geometry"));
        }
    }

    @Override
    public void register(BindingRegisterContext<Geometry> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.OTHER);
    }

    @Override
    public void set(BindingSetStatementContext<Geometry> ctx) throws SQLException {
        ctx.statement().setObject(ctx.index(), ctx.convert(converter()).value());
    }

    @Override
    public void set(BindingSetSQLOutputContext<Geometry> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(BindingGetResultSetContext<Geometry> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getObject(ctx.index()));
    }

    @Override
    public void get(BindingGetStatementContext<Geometry> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getObject(ctx.index()));
    }

    @Override
    public void get(BindingGetSQLInputContext<Geometry> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
