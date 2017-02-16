package com.example.mmalo.prototype2.Models;

/**
 * Created by mmalo on 16/02/2017.
 */

public class TutorialData {
    private String filePath;
    private boolean hasFV;
    private boolean hasDR;

    public TutorialData(String fp, boolean fv, boolean dr){
        this.setFilePath(fp);
        this.setHasFV(fv);
        this.setHasDR(dr);
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
