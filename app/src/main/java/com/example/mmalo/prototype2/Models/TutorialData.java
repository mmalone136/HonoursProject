package com.example.mmalo.prototype2.Models;

/**
 * Created by mmalo on 16/02/2017.
 */
public class TutorialData {
    private String filePath;
    private boolean hasFV;
    private boolean hasDR;
    private String picName;

    /**
     * Instantiates a new Tutorial data.
     *
     * @param fp the fp
     * @param fv the fv
     * @param dr the dr
     * @param pn the pn
     */
    public TutorialData(String fp, boolean fv, boolean dr, String pn){
        this.setFilePath(fp);
        this.setHasFV(fv);
        this.setHasDR(dr);
        this.setPicName(pn);
    }

    /**
     * Set file path.
     *
     * @param fp the fp
     */
    public void setFilePath(String fp){
        this.filePath = fp;
    }

    /**
     * Sets has fv.
     *
     * @param fv the fv
     */
    public void setHasFV(boolean fv)
    {
        this.hasFV = fv;
    }

    /**
     * Sets has dr.
     *
     * @param dr the dr
     */
    public void setHasDR(boolean dr)
    {
        this.hasDR = dr;
    }

    /**
     * Sets pic name.
     *
     * @param pn the pn
     */
    public void setPicName(String pn)
    {
        this.picName = pn;
    }

    /**
     * Get pic name string.
     *
     * @return the string
     */
    public String getPicName(){
        return this.picName;
    }

    /**
     * Get file path string.
     *
     * @return the string
     */
    public String getFilePath(){
        return this.filePath;
    }

    /**
     * Get has fv boolean.
     *
     * @return the boolean
     */
    public boolean getHasFV(){
        return this.hasFV;
    }

    /**
     * Get has dr boolean.
     *
     * @return the boolean
     */
    public boolean getHasDR(){
        return this.hasDR;
    }
}
