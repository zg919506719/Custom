// IMusic.aidl
package com.xingjian.custom;

// Declare any non-default types here with import statements

interface IMusic {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void start(String path);
    void stop(String path);
}
