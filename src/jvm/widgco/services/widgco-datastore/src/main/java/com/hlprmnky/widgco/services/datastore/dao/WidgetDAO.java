package com.hlprmnky.widgco.services.datastore.dao;

import com.hlprmnky.widgco.common.representations.Widget;
import com.hlprmnky.widgco.services.datastore.dataModel.WidgetMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(WidgetMapper.class)
public interface WidgetDAO {

    @SqlQuery("select widget_no, name from widgets where name = :widget_name")
    public Widget findByName(@Bind("widget_name") String name);

    @SqlQuery("select * from widgets")
    public List<Widget> findAll();
}
