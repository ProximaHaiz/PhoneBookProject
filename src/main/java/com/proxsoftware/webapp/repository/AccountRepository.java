package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Profile("db")
//@RepositoryDefinition(domainClass = AccountEntity.class, idClass = Long.class)
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    AccountEntity findOneByUserName(String name);

    AccountEntity findOneByEmail(String email);

    AccountEntity findOneByUserNameOrEmail(String username, String email);

    AccountEntity findOneByToken(String token);

    AccountEntity saveAndFlush(AccountEntity entity);

    /*@Modifying
    @Transactional
    @Query("DELETE FROM AccountEntity a where a.id = ?1")*/
    void deleteAccountById(long id);

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