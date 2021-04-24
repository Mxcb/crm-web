package com.crm.workbench.dao.clueDao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ClueDaoTest {
    private static ClueDao mapper;

    @BeforeClass
    public static void setUpMybatisDatabase() {
        SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(ClueDaoTest.class.getClassLoader().getResourceAsStream("mybatisTestConfiguration/ClueDaoTestConfiguration.xml"));
        //you can use builder.openSession(false) to not commit to database
        mapper = builder.getConfiguration().getMapper(ClueDao.class, builder.openSession(true));
    }

    @Test
    public void testInsertOne() throws FileNotFoundException {

    }
}
