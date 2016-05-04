package com.proxsoftware.webapp.repository;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * d
 * Created by Proxima on 22.04.2016.
 */
public abstract class AbstractXmlContactRepository<T, S extends Serializable> implements CrudRepository<T, S> {


}
