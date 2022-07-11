package com.example.application;

public class CoOrd{
    private int i;
    private int count;

    CoOrd(int in){
        i = in;
    }

    void add(){
        this.count++;
    }

    int getI(){return i;}

    int getC(){return count;}

    Integer getCount(){return (Integer) count;}

}
