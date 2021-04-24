package com.crm.settings.service;

import com.crm.settings.domain.DicType;
import com.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getDic();
}
