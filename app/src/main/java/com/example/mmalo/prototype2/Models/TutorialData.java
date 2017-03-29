package com.example.mmalo.prototype2.Models;

/**
 * Created by mmalo on 16/02/2017.
 */

public class TutorialData {
    private String filePath;
    private boolean hasFV;
    private boolean hasDR;
    private String picName;

    public TutorialData(String fp, boolean fv, boolean dr, String pn){
        this.setFilePath(fp);
        this.setHasFV(fv);
        this.setHasDR(dr);
        this.setPicName(pn);
    }

    public void setFilePath(String fp){
        this.filePath = fp;
    }

    public void setHasFV(boolean fv)
    {
        this.hasFV = fv;
    }

    public void setHasDR(boolean dr)
    {
        this.hasDR = dr;
    }

    public void setPicName(String pn)
    {
        this.picName = pn;
    }

    public String getPicName(){
        return this.picName;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public boolean getHasFV(){
        return this.hasFV;
    }

    public boolean getHasDR(){
        return this.hasDR;
    }
}
