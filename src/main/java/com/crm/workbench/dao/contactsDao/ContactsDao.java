package com.crm.workbench.dao.contactsDao;

import com.crm.workbench.domain.contacts.Contacts;

public interface ContactsDao {

    Contacts selectByName(String fullName);

    int insert(Contacts contacts);
}
