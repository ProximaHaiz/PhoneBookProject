package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.repository.XmlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Proxima on 22.04.2016.
 */
@Service(value = "xmlContactService")
//@Profile(value = "xml")
public class XmlIContactServiceImpl implements IContactService {

    @Autowired
    private XmlRepository repo;
    private AccountEntity account;
    private String accountUserName;

    public String getAccountUserName() {
        return accountUserName;
    }

    public void setAccountUserName(String accountUserName) {
        this.accountUserName = accountUserName;
    }


    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Override
    public List<ContactEntity> findAll() {
        this.accountUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        /*if (account == null) {
            account = repo.getAccount(accountUserName);
        }*/
        account = repo.getAccount(accountUserName);
        //TODO id for first contact always 0
        Map<Long, ContactEntity> contactMap = account.getContactMap();
        System.out.println("from findAll :" + contactMap);
        Collection<ContactEntity> values1 = contactMap.values();
        if (!values1.isEmpty()) {
            return values1.stream().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
       /* Collection<ContactEntity> values = repo.getAccount(accountUserName).getContactMap().values().isEmpty()
                ? repo.getAccount(accountUserName).getContactMap().values()
                : new ArrayList<>();
        //TODO make more perfomance*/
    }

    @Override
    public ContactEntity findOne(long id) {
        return account.getContactMap().get(id);
    }

    @Override
    public ContactEntity save(ContactEntity entity) {
        account.getContactMap().put(entity.getIdForXml(), entity);
        return repo.addContact(account, entity);
    }

    @Override
    public void save(Iterable<ContactEntity> entities) {
    }

    @Override
    public void delete(long id) {
        account.getContactMap().remove(id);
        repo.deleteContact(account, id);
    }
}
