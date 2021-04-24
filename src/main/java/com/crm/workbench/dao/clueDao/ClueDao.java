package com.crm.workbench.dao.clueDao;

import com.crm.workbench.domain.clue.Clue;
import org.apache.ibatis.annotations.Param;

public interface ClueDao {
    int insertOne(Clue clue);

    Clue selectById(String id);

    void deleteById(String clueId);
}
