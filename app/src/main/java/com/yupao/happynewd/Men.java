package com.yupao.happynewd;

public class Men extends People {

    public Men(String name) {
        super(name);
    }

    @Override
    public Men getPeople() {
        return new Men("men");
    }

    public People getPeople(String name) {
        return new Men(name);
    }

    public void setAnim(Men m1) {
        System.out.println("men set");
    }

    public void setOther(){

    }
}
