package com.crm.workbench.dao.clueDao;

import com.crm.workbench.domain.clue.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> selectListByClueId(String clueId);

    void delete(ClueRemark clueRemark);
}
