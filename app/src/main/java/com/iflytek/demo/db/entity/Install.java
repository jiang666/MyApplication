package com.iflytek.demo.db.entity;

/**
 * Created by jianglei on 2017/11/21.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "install")
public class Install {

    public static final String VERSION_FIELD         = "version";
    public static final String TYPE_FIELD    = "type";
    public static final String MD5_FIELD        = "md5";
    public static final String URL_FIELD        = "url";
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
    @DatabaseField(canBeNull = false, columnName = "version")
    private String version;

    /**
     * The material file resid
     */
    @DatabaseField(columnName = "type")
    private String type;

    /**
     * The material file md5 value
     */
    @DatabaseField(columnName = "md5")
    private String md5;

    /**
     * The material file url value
     */
    @DatabaseField(columnName = "url")
    private String url;

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
     * 1: download success
     */
    @DatabaseField(columnName = "state")
    private int state;

    public Install(){

    }
    public Install(String version, String type, String md5, int state, long size, long downloaded_size, String url) {
        this.version = version;
        this.type = type;
        this.md5 = md5;
        this.state = state;
        this.downloaded_size = downloaded_size;
        this.size = size;
        this.url = url;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String type) {
        this.url = url;
    }


    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


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


    public String getVersion() {
        return version;
    }

    public void setVersion(String program_id) {
        this.version = program_id;
    }


    @Override
    public String toString() {
        return "install properties:\n"
                + "id:\t[" + id + "]\n"
                + "url:\t[" + url + "]\n"
                + "version:\t[" + version + "]\n"
                + "type:\t[" + type + "]\n"
                + "md5:\t[" + md5 + "]\n"
                + "size:\t[" + size + "]\n"
                + "downloadedSize:\t[" + downloaded_size + "]\n"
                + "state:\t[" + state + "]\n";
    }
}
