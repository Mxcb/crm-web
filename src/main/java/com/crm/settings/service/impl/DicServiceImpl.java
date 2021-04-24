package com.crm.settings.service.impl;

import com.crm.settings.dao.DicTypeDao;
import com.crm.settings.dao.DicValueDao;
import com.crm.settings.domain.DicType;
import com.crm.settings.domain.DicValue;
import com.crm.settings.service.DicService;
import com.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private final DicTypeDao dicTypeDao=SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private final DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getDic() {
        Map<String,List<DicValue>> map=new HashMap<>();
        List<DicType> types= dicTypeDao.getTypeAll();
        for (DicType dicType:types){
            String code=dicType.getCode();
            List<DicValue> dicValues=dicValueDao.getListByCode(code);
            map.put(code,dicValues);
        }
        return map;
    }
}
