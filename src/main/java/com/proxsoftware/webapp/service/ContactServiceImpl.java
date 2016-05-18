package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.repository.IContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile(value = "db")
public class ContactServiceImpl implements IContactService {

    @Autowired
    private IContactRepository contactRepository;


    @Override
    public ContactEntity findOne(long id) {
        return contactRepository.findOne(id);
    }

    @Override
    public ContactEntity save(ContactEntity entity) {
        return contactRepository.save(entity);
    }

    @Override
    public void save(Iterable<ContactEntity> entities) {
        contactRepository.save(entities);
    }

    @Override
    public void delete(long id) {
        System.out.println("deleteContactEntityById from ConServ: " + id);
        contactRepository.deleteContactEntityById(id);
    }

    @Override
    public List<ContactEntity> findAllByAccount(AccountEntity account) {
        return contactRepository.findAllByUser(account);
    }
}
