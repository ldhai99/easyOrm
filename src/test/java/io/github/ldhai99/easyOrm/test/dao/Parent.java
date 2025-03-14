package io.github.ldhai99.easyOrm.test.dao;

public class Parent {
    public Parent() {
        System.out.println("parent constructor");
    }

    public static void main(String[] args) {

    }
}

class Son extends Parent {
    public Son(){
        System.out.println("son constructor");
    }
    public static void main(String[] args) {
        new Son();
    }

}