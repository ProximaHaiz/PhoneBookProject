package com.proxsoftware.webapp.profile_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by Proxima on 04.05.2016.
 */
@Component
@ComponentScan(basePackages = {"profile_test"})
public class Main {

    @Autowired
    ServiceStorage storage;

    public void display() {
        storage.printMsg();
    }
}
