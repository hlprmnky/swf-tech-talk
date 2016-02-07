package com.hlprmnky.widgco.services.datastore.dao;

import com.hlprmnky.widgco.common.representations.WidgetParts;
import com.hlprmnky.widgco.services.datastore.dataModel.WidgetPartsMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(WidgetPartsMapper.class)
public interface WidgetPartsDAO {

    @SqlQuery("select widget_parts_no, widget_no, part_no, required_qty from widget_parts where widget_no = :widget_no")
    public List<WidgetParts> getWidgetBillOfMaterials(@Bind("widget_no") int widget_no);

}
