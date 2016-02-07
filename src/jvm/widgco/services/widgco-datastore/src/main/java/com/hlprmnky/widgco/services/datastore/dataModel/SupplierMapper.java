package com.hlprmnky.widgco.services.datastore.dataModel;

import com.hlprmnky.widgco.common.representations.Supplier;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierMapper implements ResultSetMapper<Supplier> {
    public Supplier map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Supplier(resultSet.getInt("supplier_no"), resultSet.getString("name"), resultSet.getString("address"));
    }
}
