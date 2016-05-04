package com.proxsoftware.webapp.repository;

import com.proxsoftware.webapp.entity.AccountEntity;

/**
 * Created by Proxima on 29.04.2016.
 */
public abstract class AbstractXmlAccountRepository implements AccountRepository {
    //<editor-fold desc="not use">
    @Override
    public AccountEntity findOne(Long aLong) {
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
    public void delete(Iterable<? extends AccountEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }
    //</editor-fold>
}
