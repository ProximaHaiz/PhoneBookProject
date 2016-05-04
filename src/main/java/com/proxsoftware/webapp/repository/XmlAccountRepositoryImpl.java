package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "myXmlRepository")
public class XmlAccountRepositoryImpl extends AbstractXmlAccountRepository {

    @Autowired
    XmlRepository accountRepo;

    @Override
    public AccountEntity findOneByUserName(String name) {
        return accountRepo.getAccount(name);
    }

    @Override
    public AccountEntity findOneByEmail(String email) {
        return accountRepo.findAccountByEmail(email);
    }

    @Override
    public AccountEntity findOneByUserNameOrEmail(String username, String email) {
        return accountRepo.findAccountByUserNameOrEmail(username, email);
    }

    @Override
    public AccountEntity findOneByToken(String token) {
        return accountRepo.findOneByToken(token);
    }

    @Override
    public int updateUser(@Param("userName") String userName,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("middleName") String middleName) {
        accountRepo.updateAccount(userName, firstName, lastName, middleName);
        return 0;
    }

    @Override
    public <S extends AccountEntity> S save(S entity) {
        return (S) accountRepo.addAccount(entity);
    }

    @Override
    public <S extends AccountEntity> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public void delete(Long id) {
        accountRepo.deleteAccount(id);
    }

    @Override
    public void delete(AccountEntity entity) {
        accountRepo.deleteAccount(entity);
    }
}
