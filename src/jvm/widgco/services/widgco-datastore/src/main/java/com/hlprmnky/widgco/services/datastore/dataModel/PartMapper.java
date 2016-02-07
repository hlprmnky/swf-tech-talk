package com.hlprmnky.widgco.services.datastore.dataModel;

import com.hlprmnky.widgco.common.representations.Part;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartMapper implements ResultSetMapper<Part> {
    public Part map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Part(resultSet.getInt("part_no"), resultSet.getString("name"), resultSet.getInt("supplierNo"),
                resultSet.getInt("onHandQty"));
    }
}
