package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    AccountEntity findOneByUserName(String name);

    AccountEntity findOneByEmail(String email);

    AccountEntity findOneByUserNameOrEmail(String username, String email);

    AccountEntity findOneByToken(String token);

    @Modifying
    @Transactional
    @Query("update AccountEntity u set  u.firstName = :firstName, "
            + "u.lastName = :lastName, u.middleName=:middleName  where u.userName = :userName")
    int updateUser(
            @Param("userName") String userName,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("middleName") String middleName);


}