package com.hlprmnky.widgco.services.datastore.dao;

import com.hlprmnky.widgco.common.representations.Supplier;
import com.hlprmnky.widgco.services.datastore.dataModel.SupplierMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(SupplierMapper.class)
public interface SupplierDAO {

    @SqlQuery("select id from suppliers where name = :name")
    public int findIdByName(@Bind("name") String name);

    @SqlQuery("select supplierNo, name, address from suppliers where supplierNo = :supplierNo")
    public Supplier findbyId(@Bind("supplierNo") int id);
}
