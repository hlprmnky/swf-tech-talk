package com.hlprmnky.widgco.services.datastore.dao;

import com.hlprmnky.widgco.common.representations.Part;
import com.hlprmnky.widgco.services.datastore.dataModel.PartMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PartMapper.class)
public interface PartDAO {

    @SqlQuery("select part_no, name, suppier_no, on_hand_qty from parts where part_no = :part_no")
    public Part findById(@Bind("part_no") int part_no);

    @SqlUpdate("update parts set on_hand_qty = :new_qty where part_no = :part_no")
    public void updatePartOnHandQty(@Bind("part_no") int part_no, @Bind("new_qty") int new_qty);
}
