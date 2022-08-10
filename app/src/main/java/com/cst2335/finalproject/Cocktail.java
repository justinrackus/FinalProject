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

    /**
     *
     * @param name
     * @param url
     * @param instruction
     * @param ingre1
     * @param ingre2
     * @param ingre3
     */
    public Cocktail(String name, String url, String instruction, String ingre1, String ingre2, String ingre3) {
        super();
        this.name = name;
        this.url = url;
        this.instruction = instruction;
        this.ingre1 = ingre1;
        this.ingre2 = ingre2;
        this.ingre3 = ingre3;
    }

    /**
     *
     * @param name
     * @param pic
     * @param instruction
     * @param ingre1
     * @param ingre2
     * @param ingre3
     */

    public Cocktail(String name, Bitmap pic, String instruction, String ingre1, String ingre2, String ingre3) {
        super();
        this.name = name;
        this.pic = pic;
        this.instruction = instruction;
        this.ingre1 = ingre1;
        this.ingre2 = ingre2;
        this.ingre3 = ingre3;
    }

    /**
     *
     * @param name
     * @param url
     * @param instruction
     * @param ingre1
     * @param ingre2
     * @param ingre3
     * @param id
     */
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

    /**
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     *
     * @return pic
     */
    public Bitmap getPic() {
        return this.pic;
    }

    /**
     *
     * @return instruction
     */
    public String getInstruction() {
        return this.instruction;
    }

    /**
     *
     * @return ingre1
     */
    public String getIngre1() {
        return this.ingre1;
    }

    /**
     *
     * @return ingre2
     */
    public String getIngre2() {
        return this.ingre2;
    }

    /**
     *
     * @return ingre3
     */
    public String getIngre3() {
        return this.ingre3;
    }

    /**
     *
     * @return id
     */
    public long getId() {
        return this.id;
    }

    /**
     *
     * @param pic
     */
    public void setPic(Bitmap pic){
        this.pic = pic;
    }
}
