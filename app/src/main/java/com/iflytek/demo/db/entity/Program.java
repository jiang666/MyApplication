package com.iflytek.demo.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Table: programlist
 * ----------------------------------------------------------------------------
 *    id    |    name    |    programid    |    type    |     state    |   version
 * -------------------------------------------------------------------
 *   int    |varchar(255)|varchar(255)|   varchar(255)     |    0|1    |    varchar(255)
 * ----------------------------------------------------------------------------
 */
@DatabaseTable(tableName = "programlist")
public class Program {

    // Download state constants
    public static final int PLAY_DOWNLOADING_STATE  = 0;
    public static final int PLAY_DOWNLOADED_STATE   = 1;
    public static final int PLAY_UPDATING_STATE     = 2;

    public static final String NAME_FIELD       = "name";
    public static final String TYPE_FIELD       = "type";
    public static final String VERSION_FIELD       = "version";
    public static final String STATE_FIELD      = "state";
    public static final String PROGRAM_FIELD      = "programid";



    /**
     * The unique id for a play in playlist table
     */
    @DatabaseField(generatedId = true)
    private int id;
    /**
     * The play name
     */
    @DatabaseField(columnName = "name")
    private String name;
    /**
     * The program id
     */
    @DatabaseField(columnName = "programid")
    private String programid;
    /**
     * The play path relative to remote ftp server root
     */
    @DatabaseField(columnName = "type")
    private String type;
    /**
     * The play start download date
     */
    @DatabaseField(columnName = "version")
    private String version;
    /**
     * The play current download state. One of
     * 0: downloading, first download
     * 1: download or update completed
     * 2: updating, already downloaded, backend modify play, need update.
     */
    @DatabaseField(columnName = "state")
    private int state;

    /**
     * Default constructor
     */
    public Program() {

    }
    public Program(String programid, String name, String type, String version, int state) {
        this.programid = programid;
        this.name = name;
        this.type = type;
        this.version = version;
        this.state = state;
    }

    public String getProgramId() {
        return programid;
    }

    public void setProgramId(String playid) {
        this.programid = playid;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
