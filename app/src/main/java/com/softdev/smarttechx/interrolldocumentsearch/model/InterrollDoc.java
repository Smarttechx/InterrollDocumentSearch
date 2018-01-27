package com.softdev.smarttechx.interrolldocumentsearch.model;

import java.io.Serializable;

/**
 * Created by SMARTTECHX on 1/19/2018.
 */

public class InterrollDoc implements Serializable {
    public String no;
    public String part_num;
    public String part_desc;
    public String part_link;


    public String getNo() {
        return no;
    }
    public void setNo( String no) {
        this.no=no;
    }

    public String getPart_num() {
        return part_num;
    }
    public void setPart_num( String part_num) {
        this.part_num=part_num;
    }

    public String getPart_description() {
        return part_desc;
    }
    public void setPart_description( String part_desc) {
        this.part_desc=part_desc;
    }

    public String getPart_link() {
        return part_link;
    }
    public void setPart_link( String part_link) {
        this.part_link=part_link;
    }



}
