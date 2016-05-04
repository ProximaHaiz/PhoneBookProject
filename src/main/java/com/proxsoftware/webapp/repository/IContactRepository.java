package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.ContactEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Created by Proxima on 17.04.2016.
 */

@Repository(value = "db")
public interface IContactRepository extends CrudRepository<ContactEntity, Long> {

    /*@Query(value = "select c from  com.proxsoftware.webapp.entity.ContactEntityc where c. ")
    List<ContactEntity> findByName(String name);*/
}
