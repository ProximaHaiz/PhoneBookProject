package com.proxsoftware.webapp.profile_test;

import org.springframework.context.annotation.Configuration;

/**
 * Created by Proxima on 04.05.2016.
 */
@Configuration
//@Profile("impl1")
//@Service()
public class Impl1 implements ServiceStorage {
    @Override
    public void printMsg() {
        System.out.println("From impl1");
    }
}
