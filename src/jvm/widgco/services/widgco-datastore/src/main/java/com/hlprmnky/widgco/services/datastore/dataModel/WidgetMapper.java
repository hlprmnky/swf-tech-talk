package com.hlprmnky.widgco.services.datastore.dataModel;

import com.hlprmnky.widgco.common.representations.Widget;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WidgetMapper implements ResultSetMapper<Widget> {
    public Widget map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Widget(resultSet.getInt("widget_no"), resultSet.getString("name"));
    }
}
