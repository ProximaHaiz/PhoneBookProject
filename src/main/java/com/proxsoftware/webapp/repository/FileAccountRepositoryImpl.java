package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Profile("file")
public class FileAccountRepositoryImpl implements AccountRepository {

    @Autowired
    FileRepository accountRepo;

    @Override
    public AccountEntity findOneByUserName(String name) {
        return accountRepo.getAccount(name);
//        return getAccount(name);
    }

    @Override
    public AccountEntity findOneByEmail(String email) {
        return accountRepo.findAccountByEmail(email);
//        return doFindAccountByEmail(email);
    }

    @Override
    public AccountEntity findOneByUserNameOrEmail(String username, String email) {
        return accountRepo.findAccountByUserNameOrEmail(username, email);
//        return findAccountByUserNameOrEmail(username, email);
    }

    @Override
    public AccountEntity findOneByToken(String token) {
        return accountRepo.findOneByToken(token);
//        return doFindOneByToken(token);
    }

    @Override
    public int updateUser(@Param("userName") String userName,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("middleName") String middleName) {
        accountRepo.updateAccount(userName, firstName, lastName, middleName);
//        doUpdateAccount(userName, firstName, lastName, middleName);
        return 0;
    }

    @Override
    public AccountEntity saveAndFlush(AccountEntity entity) {
        return accountRepo.addAccount(entity);
//        return addAccount(entity);
    }

    //<editor-fold desc="hidden">
    @Override
    public <S extends AccountEntity> S save(S entity) {
        return (S) accountRepo.addAccount(entity);
    }

    @Override
    public <S extends AccountEntity> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public AccountEntity findOne(Long id) {
        return null;
    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public Iterable<AccountEntity> findAll() {
        return null;
    }

    @Override
    public Iterable<AccountEntity> findAll(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long aLong) {
        accountRepo.deleteAccount(aLong);
    }

    @Override
    public void delete(AccountEntity entity) {
    }

    @Override
    public void delete(Iterable<? extends AccountEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteAccountById(long id) {
        accountRepo.deleteAccount(id);
//        doDeleteAccount(id);

    }
    //</editor-fold>
}
