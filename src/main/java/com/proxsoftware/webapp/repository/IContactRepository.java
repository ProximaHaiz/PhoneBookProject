package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Proxima on 17.04.2016.
 */

@Repository()
@Profile("db")
@RepositoryDefinition(domainClass = ContactEntity.class, idClass = Long.class)
public interface IContactRepository {

    @Transactional
    List<ContactEntity> save(Iterable<ContactEntity> entities);

    ContactEntity saveAndFlush(ContactEntity entity);

    @Transactional(readOnly = true)
    ContactEntity findOne(Long id);

    @Modifying
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    ContactEntity save(ContactEntity entity);

    @Transactional(readOnly = true)
    List<ContactEntity> findAllByUser(AccountEntity entity);

    @Transactional
    @Modifying
    @Query("delete from contactEntity c where c.id=?1")
    void deleteContactEntityById(Long id);

}
