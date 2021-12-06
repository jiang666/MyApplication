package com.iflytek.demo.db.entity;

/**
 * Created by jianglei on 2017/11/21.
 */

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Face")
public class Face {

    public static final String CARDNUMBER   = "cardNumber";
    public static final String NAME         = "name";
    public static final String SEX          = "sex";
    public static final String IMAGE       = "image";

    /**
     * The material id in materials table
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * The material guid parse from configs.xml
     * Current used for bunnytouch app, lookup materials without database
     */
    @DatabaseField(canBeNull = false, columnName = "cardNumber")
    private String cardNumber;

    /**
     * The material file resid
     */
    @DatabaseField(columnName = "name")
    private String name;

    /**
     * The material file md5 value
     */
    @DatabaseField(columnName = "sex")
    private String sex;

    /**
     * The material file url value
     */
    @DatabaseField(dataType = DataType.BYTE_ARRAY ,columnName = "image")
    private byte[] bytes;


    public Face(){

    }
    public Face(String cardNumber, String name, String sex, byte[] bytes) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.sex = sex;
        this.bytes = bytes;
    }

    public static String getCARDNUMBER() {
        return CARDNUMBER;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "face properties:\n"
                + "id:\t[" + id + "]\n"
                + "name:\t[" + name + "]\n"
                + "cardNumber:\t[" + cardNumber + "]\n";
    }
}
