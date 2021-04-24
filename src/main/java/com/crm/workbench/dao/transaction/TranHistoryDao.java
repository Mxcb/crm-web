package com.crm.workbench.dao.transaction;

import com.crm.workbench.domain.transaction.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int insert(TranHistory tranHistory);

    List<TranHistory> selectHistoryById(String tranId);
}
