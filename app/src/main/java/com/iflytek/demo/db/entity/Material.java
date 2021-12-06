package com.iflytek.demo.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The material which play dependency. Such as Video/Image/Text etc.
 * -----------------------------------------------------------------------------------
 * id     guid   play_id   name   path   md5    date    size     downloaded_size
 * ------------------------------------------------------------------------------------
 */
@DatabaseTable(tableName = "materials")
public class Material {

    public static final String ID_FIELD         = "id";
    public static final String PROGRAM_ID_FIELD    = "program_id";
    public static final String RESID_FIELD       = "resid";
    //public static final String NUM_FIELD       = "num";
    public static final String MD5_FIELD        = "md5";
    //public static final String DATE_FIELD       = "date";
    public static final String SIZE_FIELD       = "size";
    public static final String DOWNLOADED_SIZE_FIELD    = "downloaded_size";
    public static final String STATE_FIELD      = "state";

    /**
     * The material id in materials table
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * The material guid parse from configs.xml
     * Current used for bunnytouch app, lookup materials without database
     */
    @DatabaseField(canBeNull = false, columnName = "program_id")
    private String program_id;

    /**
     * The foreign key related to one of playlist item
     *//*
    @DatabaseField(canBeNull = false, foreign = true, columnName = "program_id")
    private Program play;*/
    /**
     * The material file resid
     */
    @DatabaseField(columnName = "resid")
    private String resid;
    /**
     * The material file path at remote ftp server relative to ftp root
     */
    /*@DatabaseField(columnName = "num")
    private int num;
    *//**
     * The material file md5 value
     */
    @DatabaseField(columnName = "md5")
    private String md5;
    /**
     * The material file added date
     *//*
    @DatabaseField(columnName = "date")
    private Date date;*/
    /**
     * The material file size in byte unit
     */
    @DatabaseField(columnName = "size")
    private long size;
    /**
     * The material file downloaded size in byte unit
     */
    @DatabaseField(columnName = "downloaded_size")
    private long downloaded_size;
    /**
     * The material file download state, one of below
     * 0: downloading
     * 1: download or update completed
     * 2: updating
     */
    @DatabaseField(columnName = "state")
    private int state;
    public Material(){

    }
    public Material(String program_id, String resid, String md5, int state, long size, long downloaded_size) {
        this.program_id = program_id;
        this.resid = resid;
        this.md5 = md5;
        this.state = state;
        this.downloaded_size = downloaded_size;
        this.size = size;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

   /* public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }*/

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDownloaded_size() {
        return downloaded_size;
    }

    public void setDownloaded_size(long downloaded_size) {
        this.downloaded_size = downloaded_size;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /*public Program getPlay() {
        return play;
    }

    public void setPlay(Program play) {
        this.play = play;
    }*/

    public String getProgramId() {
        return program_id;
    }

    public void setProgramId(String program_id) {
        this.program_id = program_id;
    }


    @Override
    public String toString() {
        return "Material properties:\n"
                + "id:\t[" + id + "]\n"
                + "program_id:\t[" + program_id + "]\n"
                + "resid:\t[" + resid + "]\n"
                + "md5:\t[" + md5 + "]\n"
                + "size:\t[" + size + "]\n"
                + "downloadedSize:\t[" + downloaded_size + "]\n"
                + "state:\t[" + state + "]\n";
        /*return "Material properties:\n"
                + "id:\t[" + id + "]\n"
                + "name:\t[" + name + "]\n"
                + "path:\t[" + path + "]\n"
                + "md5:\t[" + md5 + "]\n"
                + "date:\t[" + date.toString() + "]\n"
                + "size:\t[" + size + "]\n"
                + "downloadedSize:\t[" + downloaded_size + "]\n"
                + "state:\t[" + state + "]\n"
                + "play_id:\t[" + ((play != null) ? play.getId() : "null") + "]\n"
                + "guid:\t[" + guid + "]\n";*/
    }
}
