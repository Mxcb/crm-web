package com.crm.workbench.dao;

import com.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int insertActivity(Activity activity);

    List<Activity> selectByLimit(Map<String, Object> map);

    Integer selectCount(Map<String, Object> map);

    int deleteById(String[] ids);

    Activity selectById(String id);

    int updateActivity(Activity activity);

    Activity selectAndUserById(String id);

    List<Activity> selectByClueId(String clueId);

    List<Activity> selectByLike(Map<String, String> map);

    List<Activity> selectLikeByName(String name);
}
