package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;

import java.util.List;

public interface IContactService {

    ContactEntity findOne(long id);

    ContactEntity save(ContactEntity entity);

    void save(Iterable<ContactEntity> entities);

    void delete(long id);

    public List<ContactEntity> findAllByAccount(AccountEntity account);
}
