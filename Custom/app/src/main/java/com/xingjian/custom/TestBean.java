package com.xingjian.custom;

/**
 * Created by thinkpad on 2018/1/17.
 */

public class TestBean {
    int age;

    @Override
    public String toString() {
        return "TestBean{" +
                "age=" + age +
                '}';
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
