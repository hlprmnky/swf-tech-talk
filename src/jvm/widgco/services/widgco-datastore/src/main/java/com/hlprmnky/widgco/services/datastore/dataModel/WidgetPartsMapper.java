package com.hlprmnky.widgco.services.datastore.dataModel;

import com.hlprmnky.widgco.common.representations.WidgetParts;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WidgetPartsMapper implements ResultSetMapper<WidgetParts> {
    public WidgetParts map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new WidgetParts(resultSet.getInt("widget_parts_no"), resultSet.getInt("widget_no"), resultSet.getInt("part_no"),
                resultSet.getInt("required_qty"));
    }
}
