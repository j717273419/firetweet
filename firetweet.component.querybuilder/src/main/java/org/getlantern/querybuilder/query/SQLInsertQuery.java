package org.getlantern.querybuilder.query;

import org.getlantern.querybuilder.OnConflict;
import org.getlantern.querybuilder.SQLQuery;
import org.getlantern.querybuilder.Utils;

public class SQLInsertQuery implements SQLQuery {

    private OnConflict onConflict;
    private String table;
    private String[] columns;
    private SQLSelectQuery select;

    SQLInsertQuery() {

    }

    @Override
    public String getSQL() {
        if (table == null) throw new NullPointerException("table must not be null!");
        final StringBuilder sb = new StringBuilder();
        sb.append("INSERT ");
        if (onConflict != null) {
            sb.append(String.format("OR %s ", onConflict.getAction()));
        }
        sb.append(String.format("INTO %s ", table));
        sb.append(String.format("(%s) ", Utils.toString(columns, ',', false)));
        sb.append(String.format("%s ", select.getSQL()));
        return sb.toString();
    }

    void setColumns(final String[] columns) {
        this.columns = columns;
    }

    void setOnConflict(final OnConflict onConflict) {
        this.onConflict = onConflict;
    }

    void setSelect(final SQLSelectQuery select) {
        this.select = select;
    }

    void setTable(final String table) {
        this.table = table;
    }

    public static final class Builder implements IBuilder<SQLInsertQuery> {

        private final SQLInsertQuery query = new SQLInsertQuery();

        private boolean buildCalled;

        @Override
        public SQLInsertQuery build() {
            buildCalled = true;
            return query;
        }

        @Override
        public String buildSQL() {
            return build().getSQL();
        }

        public Builder columns(final String[] columns) {
            checkNotBuilt();
            query.setColumns(columns);
            return this;
        }

        public Builder insertInto(final OnConflict onConflict, final String table) {
            checkNotBuilt();
            query.setOnConflict(onConflict);
            query.setTable(table);
            return this;
        }

        public Builder insertInto(final String table) {
            return insertInto(null, table);
        }

        public Builder select(final SQLSelectQuery select) {
            checkNotBuilt();
            query.setSelect(select);
            return this;
        }

        private void checkNotBuilt() {
            if (buildCalled) throw new IllegalStateException();
        }

    }

}
