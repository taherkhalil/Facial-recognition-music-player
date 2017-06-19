package com.example.noorulain.second;

/**
 * Created by Noorulain on 05-02-2016.
 */
public class Emotions {


    private int happy = 0;
    private int surprise = 0;
    private int excited = 0;


    public Emotions(int h, int s, int e){
        this.happy = h;
        this.surprise = s;
        this.excited = e;
    }

    public int getHappy() {
        return happy;
    }

    public int getSurprise() {
        return surprise;
    }

    public int getExcited() {
        return excited;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public void setSurprise(int surprise) {
        this.surprise = surprise;
    }

    public void setExcited(int excited) {
        this.excited = excited;
    }
}
