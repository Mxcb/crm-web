package com.crm.workbench.dao;

import com.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountById(String[] ids);

    int deleteByIds(String[] ids);

    List<ActivityRemark> getByActId(String activityId);

    int deleteById(String id);

    int insert(ActivityRemark remark);

    int updateById(ActivityRemark remark);
}
