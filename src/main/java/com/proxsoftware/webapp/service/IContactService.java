package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.ContactEntity;

import java.util.List;

/**
 * Created by Proxima on 19.04.2016.
 */
public interface IContactService {

    List<ContactEntity> findAll();

    ContactEntity findOne(long id);

    ContactEntity save(ContactEntity entity);

    void save(Iterable<ContactEntity> entities);

    void delete(long id);
}
