import com.crm.settings.domain.User;
import com.crm.exception.LoginException;
import com.crm.settings.service.UserService;
import com.crm.settings.service.impl.UserServiceImpl;
import com.crm.utils.DateTimeUtil;
import com.crm.utils.MD5Util;
import com.crm.utils.ServiceFactory;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.dao.transaction.TranDao;
import com.crm.workbench.domain.transaction.Tran;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TestCrm {

    @Test
    public void test() throws LoginException, JsonProcessingException {
       // UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct","yy");
        String pw=MD5Util.getMD5("123");
        map.put("loginPwd",pw);
        User user=userService.login("yy",pw,"0:0:0:0:0:0:0:1");
        System.out.println(user);
        Map<String,Boolean> map2 = new HashMap<String,Boolean>();
        map2.put("success",true);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(map2);
        System.out.println(json);
    }
    @Test
    public void test02(){
        System.out.println(UUIDUtil.getUUID());
    }

    @Test
    public void test03() throws IOException {
        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession=factory.openSession(true);
        TranDao tranDao=sqlSession.getMapper(TranDao.class);
        Tran tran=new Tran();
        for (int i = 0; i < 7; i++) {
            tran.setId(UUIDUtil.getUUID());
            tran.setStage("01资质审查");
            tranDao.insert(tran);
        }
    }
}
