package com.proxsoftware.webapp.profile_test;

/**
 * Created by Proxima on 04.05.2016.
 */
//@Profile("impl2")
//@Component("impl2")
public class Impl2 implements ServiceStorage {
    @Override
    public void printMsg() {
        System.out.println("From impl2");
    }
}
