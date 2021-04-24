package com.crm.workbench.service.impl;

import com.crm.utils.DateTimeUtil;
import com.crm.utils.SqlSessionUtil;
import com.crm.utils.UUIDUtil;
import com.crm.workbench.dao.ActivityDao;
import com.crm.workbench.dao.clueDao.ClueActivityRelationDao;
import com.crm.workbench.dao.clueDao.ClueDao;
import com.crm.workbench.dao.clueDao.ClueRemarkDao;
import com.crm.workbench.dao.contactsDao.ContactsActivityRelationDao;
import com.crm.workbench.dao.contactsDao.ContactsDao;
import com.crm.workbench.dao.contactsDao.ContactsRemarkDao;
import com.crm.workbench.dao.customerDao.CustomerDao;
import com.crm.workbench.dao.customerDao.CustomerRemarkDao;
import com.crm.workbench.dao.transaction.TranDao;
import com.crm.workbench.dao.transaction.TranHistoryDao;
import com.crm.workbench.domain.Activity;
import com.crm.workbench.domain.clue.Clue;
import com.crm.workbench.domain.clue.ClueActivityRelation;
import com.crm.workbench.domain.clue.ClueRemark;
import com.crm.workbench.domain.contacts.Contacts;
import com.crm.workbench.domain.contacts.ContactsActivityRelation;
import com.crm.workbench.domain.contacts.ContactsRemark;
import com.crm.workbench.domain.customer.Customer;
import com.crm.workbench.domain.customer.CustomerRemark;
import com.crm.workbench.domain.transaction.Tran;
import com.crm.workbench.domain.transaction.TranHistory;
import com.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao cActRelDao=
            SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public boolean save(Clue clue) {
        boolean flag=true;
        int count=clueDao.insertOne(clue);
        if (count!=1) flag=false;
        return flag;
    }

    @Override
    public Clue clueDetail(String id) {
        return clueDao.selectById(id);
    }

    @Override
    public boolean unbind(String id) {
        boolean flag=true;
        int count=cActRelDao.deleteById(id);
        if (count!=1) flag=false;
        return flag;
    }

    @Override
    public boolean bind(String[] aids, String cid) {
        int count=0;
        ClueActivityRelation clueActivityRelation=new ClueActivityRelation();
        for (String aid:aids){
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(cid);
            clueActivityRelation.setActivityId(aid);
            count+=cActRelDao.insert(clueActivityRelation);
        }
        System.out.println(count == aids.length);
        return count == aids.length;
    }

    @Override
    public boolean convert(Tran tran, String clueId, String createBy) {
        boolean flag=true;
        Clue clue=clueDao.selectById(clueId);
        Customer customer=customerDao.selectByName(clue.getCompany());
        int cusCount=0;
        if (customer==null){
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(clue.getCompany());
            customer.setAddress(clue.getAddress());
            customer.setOwner(clue.getOwner());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            cusCount=customerDao.add(customer);
        }

        int conCount=0;
        Contacts contacts=contactsDao.selectByName(clue.getFullname());
        if (contacts==null){
            contacts=new Contacts();
            contacts.setId(UUIDUtil.getUUID());
            contacts.setFullname(clue.getFullname());
            contacts.setOwner(clue.getOwner());
            contacts.setMphone(clue.getMphone());
            contacts.setCustomerId(customer.getId());
            conCount=contactsDao.insert(contacts);
        }

        List<ClueRemark> clueRemarks=clueRemarkDao.selectListByClueId(clueId);
        int conRemCount=0;
        int cusRemCount=0;
        for (ClueRemark clueRemark:clueRemarks){
            String noteContent=clueRemark.getNoteContent();

            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setEditFlag("0");
            conRemCount=contactsRemarkDao.insert(contactsRemark);

            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setEditFlag("0");
            cusRemCount=customerRemarkDao.insert(customerRemark);
        }

        int conActRelCount=0;
        List<ClueActivityRelation> clueActivityRelations=cActRelDao.selectByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation:clueActivityRelations){
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());
            conActRelCount=contactsActivityRelationDao.insert(contactsActivityRelation);
        }

        int tranCount=0;
        int tranHistoryCount=0;
        if (tran!=null){
            tran.setContactsId(contacts.getId());
            tran.setSource(clue.getSource());
            tran.setCustomerId(customer.getId());
            tranCount=tranDao.insert(tran);

            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistoryCount=tranHistoryDao.insert(tranHistory);
        }

        for (ClueRemark clueRemark:clueRemarks){
            clueRemarkDao.delete(clueRemark);
        }

        for (ClueActivityRelation clueActivityRelation:clueActivityRelations){
            cActRelDao.delete(clueActivityRelation);
        }

        clueDao.deleteById(clueId);

        if (conCount!=1||cusCount!=1||conRemCount!=1||
                cusRemCount!=1||conActRelCount!=1||tranCount!=1||tranHistoryCount!=1){
            flag=false;
        }
        return flag;
    }
}
