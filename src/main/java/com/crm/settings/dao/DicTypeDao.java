package com.crm.settings.dao;

import com.crm.settings.domain.DicType;
import com.crm.settings.domain.DicValue;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DicTypeDao {
    List<DicType> getTypeAll();
}
