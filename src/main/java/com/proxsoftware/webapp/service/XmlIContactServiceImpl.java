package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Created by Proxima on 22.04.2016.
 */
@Service()
@Lazy
@Profile(value = "file")
public class XmlIContactServiceImpl implements IContactService {

    @Autowired
    private FileRepository repo;

    private AccountEntity account;


    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Override
    public List<ContactEntity> findAllByAccount(AccountEntity account) {
        if (Objects.isNull(this.account)) {
            this.account = account;
        }
        return repo.findAllContacts(account);
    }

    @Override
    public ContactEntity findOne(long id) {
        return account.getContactMap().get(id);
    }

    @Override
    public ContactEntity save(ContactEntity entity) {
        ContactEntity saveContact = repo.addContact(account, entity);
        account.getContactMap().put(saveContact.getId(), saveContact);
        return saveContact;
    }
    public ContactEntity save(ContactEntity entity,AccountEntity account) {
        ContactEntity saveContact = repo.addContact(account, entity);
        account.getContactMap().put(saveContact.getId(), saveContact);
        return saveContact;
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
