package com.crm.workbench.service;

import com.crm.workbench.domain.clue.Clue;
import com.crm.workbench.domain.transaction.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    Clue clueDetail(String id);

    boolean unbind(String id);

    boolean bind(String[] aids,String cid);

    boolean convert(Tran tran,String clueId,String createBy);
}
