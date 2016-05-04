package com.proxsoftware.webapp.service;

import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.repository.IContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements IContactService {

    @Autowired()
    private IContactRepository IContactRepository;

    @Override
    public List<ContactEntity> findAll() {
        return (List<ContactEntity>) IContactRepository.findAll();
    }

    @Override
    public ContactEntity findOne(long id) {
        return IContactRepository.findOne(id);
    }

    @Override
    public ContactEntity save(ContactEntity entity) {
        return IContactRepository.save(entity);
    }

    @Override
    public void save(Iterable<ContactEntity> entities) {
        IContactRepository.save(entities);
    }

    @Override
    public void delete(long id) {
        IContactRepository.delete(id);
    }
}
