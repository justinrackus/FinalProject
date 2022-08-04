package com.cst2335.finalproject;

import android.graphics.Bitmap;

public class Cocktail {
    String name;
    String url;
    Bitmap pic;
    String instruction;
    String ingre1;
    String ingre2;
    String ingre3;
    long id;

    public Cocktail(String name, String url, String instruction, String ingre1, String ingre2, String ingre3) {
        super();
        this.name = name;
        this.url = url;
        this.instruction = instruction;
        this.ingre1 = ingre1;
        this.ingre2 = ingre2;
        this.ingre3 = ingre3;
    }

    public Cocktail(String name, Bitmap pic, String instruction, String ingre1, String ingre2, String ingre3) {
        super();
        this.name = name;
        this.pic = pic;
        this.instruction = instruction;
        this.ingre1 = ingre1;
        this.ingre2 = ingre2;
        this.ingre3 = ingre3;
    }
    public Cocktail(String name, String url, String instruction, String ingre1, String ingre2, String ingre3, long id) {
        super();
        this.name = name;
        this.url = url;
        this.instruction = instruction;
        this.ingre1 = ingre1;
        this.ingre2 = ingre2;
        this.ingre3 = ingre3;
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public String getUrl() {
        return this.url;
    }
    public Bitmap getPic() {
        return this.pic;
    }
    public String getInstruction() {
        return this.instruction;
    }
    public String getIngre1() {
        return this.ingre1;
    }
    public String getIngre2() {
        return this.ingre2;
    }
    public String getIngre3() {
        return this.ingre3;
    }
    public long getId() {
        return this.id;
    }
    public void setPic(Bitmap pic){
        this.pic = pic;
    }
}
