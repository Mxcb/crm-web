package com.crm.workbench.service;

import com.crm.utils.SqlSessionUtil;
import com.crm.vo.Pagination;
import com.crm.workbench.dao.ActivityDao;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    Pagination<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean updateById(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByActId(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark remark);

    boolean updateRemark(ActivityRemark remark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> selectByLikeName(Map<String, String> map);

    List<Activity> getActivityListByName(String name);
}
