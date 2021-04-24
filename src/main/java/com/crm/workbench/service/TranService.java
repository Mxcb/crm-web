package com.crm.workbench.service;

import com.crm.workbench.domain.transaction.Tran;
import com.crm.workbench.domain.transaction.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> selectHistoryById(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();
}
