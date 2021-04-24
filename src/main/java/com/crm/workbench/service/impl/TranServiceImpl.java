package com.crm.workbench.service.impl;

import com.crm.utils.DateTimeUtil;
import com.crm.utils.SqlSessionUtil;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.dao.customerDao.CustomerDao;
import com.crm.workbench.dao.transaction.TranDao;
import com.crm.workbench.dao.transaction.TranHistoryDao;
import com.crm.workbench.domain.customer.Customer;
import com.crm.workbench.domain.transaction.Tran;
import com.crm.workbench.domain.transaction.TranHistory;
import com.crm.workbench.service.TranService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public static void main(String[] args) throws IOException {
////        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
////        System.out.println(inputStream.toString());
////        FileOutputStream fos=new FileOutputStream("xm.txt");
////        byte[] bytes=new byte[10];
////        while (inputStream.read(bytes)!=-1){
////            for (int i = 0; i < bytes.length; i++) {
////                System.out.print(bytes[i]+" ");
////            }
////            System.out.println(new String(bytes));
////            fos.write(bytes);
////            fos.flush();
////        }
////        SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(inputStream);
////        SqlSession sqlSession=factory.openSession();
//        System.out.println(0xff);
    }

    @Override
    public boolean save(Tran tran, String customerName) {
        Customer customer=customerDao.selectByName(customerName);
        int countCus=0;
        boolean flag=true;
        if (customer!=null){
            tran.setCustomerId(customer.getId());
        }else {
            Customer cus=new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            tran.setCustomerId(cus.getId());
            countCus=customerDao.insert(cus);
        }
        int countTran=tranDao.insert(tran);

        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        int countTranHis=tranHistoryDao.insert(tranHistory);
        if (countTran!=1 || countCus!=1 ||countTranHis!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        return tranDao.selectById(id);
    }

    @Override
    public List<TranHistory> selectHistoryById(String tranId) {
        return tranHistoryDao.selectHistoryById(tranId);
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
        int count=tranDao.update(t);
        if (count!=1) flag=false;
        if (flag){
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateBy(t.getEditBy());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setTranId(t.getId());
            int countTranHis=tranHistoryDao.insert(tranHistory);
            if (countTranHis!=1) flag=false;
        }
        return flag;
    }

    @Override
    public Map<String,Object> getCharts() {
        List<Map<String,Object>> list=tranDao.selectByGroup();
        int countTran=tranDao.getTotal();
        Map<String,Object> map=new HashMap<>();
        map.put("total",countTran);
        map.put("dataList",list);
        return map;
    }
}
