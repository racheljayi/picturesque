package com.example.application;

import java.awt.*;

public class CC {
    private Color c;

    CC(Color in){
        c = in;
    }

    Color getC(){
        return c;
    }

    int getR(){
        return c.getRed();
    }

    int getG(){
        return c.getGreen();
    }

    int getB(){
        return c.getBlue();
    }

    String getHex() { return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()); }

}
